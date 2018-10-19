set global log_bin_trust_function_creators=1;

CREATE FUNCTION sayHello()  
RETURNS VARCHAR(300)  
BEGIN  
	
  RETURN 'Hello, world!';  
END;

select sayHello();
drop FUNCTION sayHello