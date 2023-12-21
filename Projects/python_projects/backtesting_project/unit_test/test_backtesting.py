#!/usr/bin/env python3
import unittest


class Test(unittest.TestCase):
    def test_1(self):
        self.assertEqual(1, 1)

    def test_2(self):
        self.assertEqual(1, 1)


if __name__ == '__main__':
    unittest.main()
