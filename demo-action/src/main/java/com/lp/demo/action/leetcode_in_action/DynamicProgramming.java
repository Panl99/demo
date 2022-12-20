package com.lp.demo.action.leetcode_in_action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @create 2020/12/1 22:40
 * @auther outman
 * @description 动态规划（英语：Dynamic programming，简称 DP）是一种在数学、管理科学、计算机科学、经济学和生物信息学中使用的，通过把原问题分解为相对简单的子问题的方式求解复杂问题的方法。
 *              动态规划常常适用于有重叠子问题和最优子结构性质的问题，动态规划方法所耗时间往往远少于朴素解法。
 *              基本思想: 将给定问题，解其不同部分(子问题)，根据子问题的解，得出原问题解。
 *              动态规划 优于递归，如：斐波那契数列
 * 1. 整数拆分（多少种分法）
 * 2. 整数拆分（最大乘积）
 * 3. 青蛙过河
 * 4. 斐波那切数列第n项
 * 5. 青蛙跳台阶问题分析（实质上就是斐波那切数列）
 **/
public class DynamicProgramming {
    public static void main(String[] args) {
        //整数拆分
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            int temp = integerBreak(n, m);
            System.out.print(temp);
        }

        int n = scanner.nextInt();

        //斐波那切数列第n项
        long fibonacci = fibonacci(n);
        System.out.println("fibonacci = " + fibonacci);

        // 递归实现
        int jumpFloorByRecursion = jumpFloorByRecursion(n);
        System.out.println("jumpFloorByRecursion = " + jumpFloorByRecursion);
        // 迭代实现
        int jumpFloorByIteration = jumpFloorByIteration(n);
        System.out.println("jumpFloorByIteration = " + jumpFloorByIteration);
    }
    /**
     * 标签：动态规划
     * 题目：整数拆分（多少种分法）
     * 描述：将正整数n无序拆分成最大数为m的拆分方案个数，要求所有拆分方案不重复。
     * 示例：
     *      n = 5, m = 5,对应的拆分方案如下：
     *      5 = 5
     *      5 = 4 + 1
     *      5 = 3 + 2
     *      5 = 3 + 1 + 1
     *      5 = 2 + 2 + 1
     *      5 = 2 + 1 + 1 + 1
     *      5 = 1 + 1 + 1 + 1 + 1
     * 思路：假设拆分方案个数：f(m,n)
     *      ①当 m=1 时，只有一种：f(1,n)=1+1+...+1（n个1相加）
     *      ②当 n=1 时，只有1种(m只能为1)：f(1,1)=1
     *      ③当 m>n 时，不存在，f(m,n)=0
     *      ④当 m=n 时，f(m,n)=f(m-1,n)+1，分两种情况：
     *          划分包含m，只有1种：f(m,n)=1
     *          划分不包含m，f(m,n)=f(m-1,n)
     *      ⑤当 m<n 时，f(m,n)=f(m,n-m)+f(m-1,n)，分两种情况：
     *          划分包含m，f(m,n)=f(m,n-m)
     *          划分不包含m，f(m,n)=f(m-1,n)
     * 注意：代码n，m位置与思路相反
     */
    public static int integerBreak(int n, int m) {
        int dp[][] = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (i == 1 || j == 1) {
                    dp[i][j] = 1;
                } else if (i == j) {
                    dp[i][j] = 1 + dp[i][j - 1];
                } else if (i < j) {
                    dp[i][j] = dp[i][i];
                } else {
                    dp[i][j] = dp[i - j][j] + dp[i][j - 1];
                }
            }
        }
        return dp[n][m];
    }

    /**
     * 链接：https://leetcode-cn.com/problems/integer-break/
     * 标签：动态规划
     * 题目：343. 整数拆分（最大乘积）
     * 描述：给定一个正整数 n，将其拆分为至少两个正整数的和，并使这些整数的乘积最大化。 返回你可以获得的最大乘积。
     * 示例：
     *      输入: 2
     *      输出: 1
     *      解释: 2 = 1 + 1, 1 × 1 = 1。
     *
     *      输入: 10
     *      输出: 36
     *      解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36。
     * 说明: 你可以假设 n 不小于 2 且不大于 58。
     * 思路：
     *
     * 解法：
     *      //动态规划 时间复杂度：O(n^2) 空间复杂度：O(n)
     *      //优化动态规划 时间复杂度：O(n) 空间复杂度：O(n)
     *      //数学
     *      时间复杂度：O(1)。涉及到的操作包括计算商和余数，以及幂次运算，时间复杂度都是常数。
     *      空间复杂度：O(1)。只需要使用常数复杂度的额外空间。
     *
     */
    //动态规划
    public int integerBreak1(int n) {
        int[] dp = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            int curMax = 0;
            for (int j = 1; j < i; j++) {
                curMax = Math.max(curMax, Math.max(j * (i - j), j * dp[i - j]));
            }
            dp[i] = curMax;
        }
        return dp[n];
    }

    //优化动态规划
    public int integerBreak2(int n) {
        if (n < 4) {
            return n - 1;
        }
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 3; i <= n; i++) {
            dp[i] = Math.max(Math.max(2 * (i - 2), 2 * dp[i - 2]), Math.max(3 * (i - 3), 3 * dp[i - 3]));
        }
        return dp[n];
    }

    //数学
    public int integerBreak3(int n) {
        if (n <= 3) {
            return n - 1;
        }
        int quotient = n / 3;
        int remainder = n % 3;
        if (remainder == 0) {
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            return (int) Math.pow(3, quotient - 1) * 4;
        } else {
            return (int) Math.pow(3, quotient) * 2;
        }
    }

    /**
     * 链接：https://leetcode-cn.com/problems/frog-jump/
     * 标签：动态规划
     * 题目：403. 青蛙过河 [困难]
     * 描述：一只青蛙想要过河。 假定河流被等分为 x 个单元格，并且在每一个单元格内都有可能放有一石子（也有可能没有）。 青蛙可以跳上石头，但是不可以跳入水中。
     *      给定石子的位置列表（用单元格序号升序表示）， 请判定青蛙能否成功过河（即能否在最后一步跳至最后一个石子上）。 开始时， 青蛙默认已站在第一个石子上，并可以假定它第一步只能跳跃一个单位（即只能从单元格1跳至单元格2）。
     *      如果青蛙上一步跳跃了 k 个单位，那么它接下来的跳跃距离只能选择为 k - 1、k 或 k + 1个单位。 另请注意，青蛙只能向前方（终点的方向）跳跃。
     * 请注意：
     *      石子的数量 ≥ 2 且 < 1100；
     *      每一个石子的位置序号都是一个非负整数，且其 < 231；
     *      第一个石子的位置永远是0。
     * 示例1：
     *      [0,1,3,5,6,8,12,17]
     *      总共有8个石子。
     *      第一个石子处于序号为0的单元格的位置, 第二个石子处于序号为1的单元格的位置,
     *      第三个石子在序号为3的单元格的位置， 以此定义整个数组...
     *      最后一个石子处于序号为17的单元格的位置。
     *
     *      返回 true。即青蛙可以成功过河，按照如下方案跳跃：
     *      跳1个单位到第2块石子, 然后跳2个单位到第3块石子, 接着
     *      跳2个单位到第4块石子, 然后跳3个单位到第6块石子,
     *      跳4个单位到第7块石子, 最后，跳5个单位到第8个石子（即最后一块石子）。
     * 示例2：
     *      [0,1,2,3,4,8,9,11]
     *      返回 false。青蛙没有办法过河。
     *      这是因为第5和第6个石子之间的间距太大，没有可选的方案供青蛙跳跃过去。
     * 思路：
     *      给定石子的位置列表（用单元格序号升序表示），请判定青蛙能否成功过河（即能否在最后一步跳至最后一个石子上）。 开始时， 青蛙默认已站在第一个石子上，并可以假定它第一步只能跳跃一个单位（即只能从单元格 1 跳至单元格 2）。如果青蛙上一步跳跃了 k 个单位，那么它接下来的跳跃距离只能选择为 k - 1、k 或 k + 1个单位。
     *      在动态规划方法中，我们会利用散列表 mapmap，对于散列表中的 key:valuekey:value，keykey 表示当前石头的位置，valuevalue 是一个包含 jumpsizejumpsize 的集合，其中每个 jumpsizejumpsize 代表可以通过大小为 jumpysizejumpysize 的一跳到达当前位置。
     *          首先我们对散列表初始化，keykey 为所有石头的位置，除了位置 0 对应的 valuevalue 为包含一个值 0 的集合以外，其余都初始化为空集。接下来，依次遍历每个位置上的石头。对于每个 currentPositioncurrentPosition，遍历 valuevalue 中每个 jumpsizejumpsize，判断位置 currentPosition + newjumpsizecurrentPosition+newjumpsize 是否存在于 mapmap 中，对于每个 jumpsizejumpsize，newjumpsizenewjumpsize 分别为 jumpsize-1jumpsize−1，jumpsizejumpsize，jumpsize+1jumpsize+1。如果找到了，就在对应的 valuevalue 集合里新增 newjumpsizenewjumpsize。重复这个过程直到结束。如果在结束的时候，最后一个位置对应的集合非空，那也就意味着我们可以到达终点，如果还是空集那就意味着不能到达终点。
     *
     * 时间复杂度：O(n^2)。嵌套两层循环
     * 空间复杂度：O(n^2)。hashmap大小最大为n^2
     */
    public boolean canCross(int[] stones) {
        HashMap<Integer, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < stones.length; i++) {
            map.put(stones[i], new HashSet<Integer>());
        }
        map.get(0).add(0);
        for (int i = 0; i < stones.length; i++) {
            for (int k : map.get(stones[i])) {
                for (int step = k - 1; step <= k + 1; step++) {
                    if (step > 0 && map.containsKey(stones[i] + step)) {
                        map.get(stones[i] + step).add(step);
                    }
                }
            }
        }
        return map.get(stones[stones.length - 1]).size() > 0;
    }

    /**
     * 求斐波那切数列第n项
     * 斐波那切数列公式：
     *      f(n) = 0；n=0时
     *      f(n) = 1；n=1时
     *      f(n) = f(n-1) + f(n-2)；n>=1时
     */
    public static long fibonacci(int n) {
        long fibOne = 1;
        long fibTwo = 0;
        long fibN = 0;

        int[] init = {0,1};
        if (n < 2) {
            return init[n];
        }

        for (int i = 2; i <= n; i++) {
//            System.out.println("fibN = "+fibN +",fibTwo = "+fibTwo +",fibOne = "+fibOne);
            fibN = fibOne + fibTwo;
            fibTwo = fibOne;
            fibOne = fibN;

        }
        return fibN;
    }
    /**
     * 青蛙跳台阶问题
     * 描述：一只青蛙一次可以跳上1级台阶，也可以跳上2级台阶。求该青蛙跳上一个n级台阶总共多少种跳法？
     * 思路：
     *      n=1；1种跳法。
     *      n=2；2种跳法。1+1、2
     *      假设n级台阶跳法数为f(n)，n>2时，第一次跳就分两种情况：
     *              第一次跳1级；后面剩下n-1级台阶跳法数就为：f(n-1)
     *              第一次跳2级；后面剩下n-2级台阶跳法数就为：f(n-2)
     *      因此，n级台阶跳法数和为：f(n) = f(n-1) + f(n-2)
     *      实质上就是上边的斐波那切数列了
     */
    // 递归实现
    public static int jumpFloorByRecursion(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        return jumpFloorByRecursion(n - 1) + jumpFloorByRecursion(n - 2);
    }

    // 迭代实现
    public static int jumpFloorByIteration(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int f1 = 1;
        int f2 = 2;
        int result = 0;
        for (int i = 3; i <= n; i++) {
            result = f1 + f2;
            f1 = f2;
            f2 = result;
        }
        return result;
    }


}
