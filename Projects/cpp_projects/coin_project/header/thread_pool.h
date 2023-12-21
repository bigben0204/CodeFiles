#ifndef GTEST_PROJECT_THREAD_POOL_H
#define GTEST_PROJECT_THREAD_POOL_H

#include <queue>
#include <mutex>
#include <thread>
#include <condition_variable>
#include <future>
#include <functional>

template<typename T>
class SafeQueue {
public:
    SafeQueue() {
    }

    SafeQueue(SafeQueue&& other) {
    }

    ~SafeQueue() {
    }

    bool empty() // 返回队列是否为空
    {
        std::unique_lock<std::mutex> lock(mutex_); // 互斥信号变量加锁，防止m_queue被改变
        return queue_.empty();
    }

    int size() {
        std::unique_lock<std::mutex> lock(mutex_); // 互斥信号变量加锁，防止m_queue被改变
        return queue_.size();
    }

    // 队列添加元素
    void enqueue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_);
        queue_.emplace(t);
    }

    // 队列取出元素
    bool dequeue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_); // 队列加锁
        if (queue_.empty()) {
            return false;
        }
        t = std::move(queue_.front()); // 取出队首元素，返回队首元素值，并进行右值引用
        queue_.pop(); // 弹出入队的第一个元素
        return true;
    }

private:
    std::queue<T> queue_; //利用模板函数构造队列
    std::mutex mutex_; // 访问互斥信号量
};

class ThreadPool {
public:
    // 线程池构造函数
    ThreadPool(const int n_threads = -1)
        : isInit_(false), isShutdown_(false),
          threads_(n_threads > 0 ? n_threads : std::thread::hardware_concurrency()) {
    }

    ~ThreadPool() {
        if (!isShutdown_) {
            shutdown();
        }
    }

    ThreadPool(const ThreadPool&) = delete;

    ThreadPool(ThreadPool&&) = delete;

    ThreadPool& operator=(const ThreadPool&) = delete;

    ThreadPool& operator=(ThreadPool&&) = delete;

    // Inits thread pool
    bool init() {
        if (!isInit_) {
            for (int i = 0; i < threads_.size(); ++i) {
                threads_.at(i) = std::thread(ThreadWorker(this, i)); // 分配工作线程
            }
            isInit_ = true;
        }
        return isInit_;
    }

    // Waits until threads finish their current task and shutdowns the pool
    void shutdown() {
        isShutdown_ = true;
        conditionalLock_.notify_all(); // 通知，唤醒所有工作线程

        for (int i = 0; i < threads_.size(); ++i) {
            if (threads_.at(i).joinable()) // 判断线程是否在等待
            {
                threads_.at(i).join(); // 将线程加入到等待队列
            }
        }
    }

    // Submit a function to be executed asynchronously by the pool
    template<typename F, typename... Args>
    auto submit(F&& f, Args&& ...args) -> std::future<decltype(f(args...))> {
        static bool dummyInit = init();
        // Create a function with bounded parameter ready to execute
        std::function<decltype(f(args...))()> func = std::bind(std::forward<F>(f),
                                                               std::forward<Args>(args)...); // 连接函数和参数定义，特殊函数类型，避免左右值错误

        // Encapsulate it into a shared pointer in order to be able to copy construct
        auto task_ptr = std::make_shared<std::packaged_task<decltype(f(args...))() >>(func);

        // Warp packaged task into void function
        std::function<void()> warpper_func = [task_ptr]() {
            (*task_ptr)();
        };

        // 队列通用安全封包函数，并压入安全队列
        queue_.enqueue(warpper_func);
        // 唤醒一个等待中的线程
        conditionalLock_.notify_one();
        // 返回先前注册的任务指针
        return task_ptr->get_future();
    }

private:
    class ThreadWorker // 内置线程工作类
    {
    public:
        // 构造函数
        ThreadWorker(ThreadPool* pool, const int id) : threadPool_(pool), id_(id) {
        }

        // 重载()操作
        void operator()() {
            std::function<void()> func; // 定义基础函数类func

            bool dequeued; // 是否正在取出队列中元素
            while (!threadPool_->isShutdown_) {
                {
                    // 为线程环境加锁，互访问工作线程的休眠和唤醒
                    std::unique_lock<std::mutex> lock(threadPool_->conditionalMutex_);

                    // 如果任务队列为空，阻塞当前线程
                    if (threadPool_->queue_.empty()) {
                        threadPool_->conditionalLock_.wait(lock); // 等待条件变量通知，开启线程
                    }

                    // 取出任务队列中的元素
                    dequeued = threadPool_->queue_.dequeue(func);
                }

                // 如果成功取出，执行工作函数
                if (dequeued) {
                    func();
                }
            }
        }

    private:
        int id_; // 工作id
        ThreadPool* threadPool_; // 所属线程池
    };

private:
    bool isInit_; // 线程池是否关闭
    bool isShutdown_; // 线程池是否关闭
    std::vector<std::thread> threads_; // 工作线程队列
    SafeQueue<std::function<void()>> queue_; // 执行函数安全队列，即任务队列
    std::mutex conditionalMutex_; // 线程休眠锁互斥变量
    std::condition_variable conditionalLock_; // 线程环境锁，可以让线程处于休眠或者唤醒状态
};

#endif //GTEST_PROJECT_THREAD_POOL_H