牛客网：[华为机试：https://www.nowcoder.com/ta/huawei/](https://www.nowcoder.com/ta/huawei/)

- 注意：
	- 需要自己导包；
	- 需要自己输入输出；scanner、sout
	- 类名：Main 方法名：public static void main()

1.	字符串最后一个单词的长度
```java

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine().trim();

    //     String[] arr = s.split(" ");
    //     int length = arr[arr.length - 1].length();
    //     System.out.println(length);

        System.out.println(s.length() - s.lastIndexOf(" ") - 1);
    }
}

```

2. 计算字符个数
```java
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine().toLowerCase();
        String ch = scanner.nextLine().toLowerCase();
//        int count = 0;
//        for (String c: s.split("")) {
//            if (ch.equalsIgnoreCase(c)) {
//                count++;
//            }
//        }
//        System.out.println(count);
        System.out.println(s.length() - s.replaceAll(ch, "").length());
    }
}
```

3. 明明的随机数【去重排序 用TreeSet】
```java
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            int count = scanner.nextInt();
            TreeSet treeSet = new TreeSet();
            for (int i = 0; i < count; i++) {
                treeSet.add(scanner.nextInt());
            }
            Iterator iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
}
```


4. 字符串分割
```java
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()){
            String next = sc.next();
            while (next.length()>8){
                String substring = next.substring(0, 8);
                System.out.println(substring);
                next = next.substring(8);
            }
            int tmp = 8 - next.length();
            for (int i = 0; i < tmp; i++) {
                next += "0";
            }
            System.out.println(next);
        }
    }
}
```

5. 进制转换（16 -> 10）
```java
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()){
            System.out.println(Integer.parseInt(sc.next().replaceAll("x",""),16));
        }
    }
}
```
- 正十进制小数m转为n进制小数
    - 编写程序实现任意十进制正小数m转换成n进制的正小数，小数点后保留10位小数。
    - 输入包含两个数m，n，用空格隔开。输入包含多组测试，当m，n都为0时输入结束。
```java
import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		StringBuffer sb = new StringBuffer();
		//把所有的answer都存入字符串缓冲池，一次性输出。
		while(true){
			double m = sc.nextDouble();	//接收的m是小数	
			int n = sc.nextInt();//要转化为n进制，是整数
			if((m == 0)&&(n == 0)) {//若都为0时，输出字符串缓冲池的全部内容
				System.out.print(sb);
				break;//并停止
			}
			sb.append("0.");
			for(int i : getm(m,n)) {
				sb.append(String.valueOf(i));//将int型转换为String类型。
			}
			sb.append("\n");
		}

	}
	private static int[] getm(Double m,int p){
		Double n = (double)p;//将in型强制转换为double类型，为下面的乘法作准备
		int[] T = new int[10];//因为小数点后要保留十位小数		
		for(int index = 0;index < 10;index++) {
			m = n*m;//不能让int*double，会转成int
			int i  = new Double(m).intValue();//double类型强制转换成int类型的写法。不能强制转换。
			T[index] = i;
			if(m >= 1.0) {
				m = m - i;
			}
		}
		return T;//返回的是一个int数组
	}
}
```

6. 质数因子
```java
import java.util.*;

public class Main{
    public static void handler(int num) {
        while(num != 1) {
            for(int i = 2; i <= num; ++i) {
                if( num % i == 0) {
                    System.out.print( i + " ");
                    num = num / i;
                    break;
                }
            }    
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            handler(sc.nextInt());
        }
        
    }
}
```

7. 取近似值（四舍五入）
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double db = sc.nextDouble();
        System.out.println(Math.round(db));
        
    }
}
```

8. 合并表记录（相同key的value相加，结果排序）
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int count = Integer.valueOf(sc.nextLine());
            Map<Integer, Integer> map = new TreeMap();
            for (int i = 0; i < count; i++) {
                String[] array = sc.nextLine().split(" ");
                Integer key = Integer.valueOf(array[0]);
                Integer num = Integer.valueOf(array[1]);
                if (map.containsKey(key)) {
                    Integer value = map.get(key) + num;
                    map.put(key, value);
                } else {
                    map.put(key, num);
                }
            }
            for (Integer key : map.keySet()) {
                System.out.println(key + " " + map.get(key));
            }
        }
    }
}
```

9. 提取不重复整数
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        char[] ch = (num + "").toCharArray();
        String str = "";
        for(int i = ch.length - 1; i >= 0; i--){
            if(!str.contains(ch[i] + "")){
                str += ch[i];
            }
        }
        System.out.println(Integer.valueOf(str));
    }
}
```

10. 字符个数统计（凡是涉及到去重统计都可以用位图实现。因为每一个不同的数据只需要用二进制的一位存储即可，大大减小了统计所使用的存储空间）
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.next();
        //总共有128个字符。字需要用128位
        BitSet bitSet = new BitSet(128);
        for (char c : line.toCharArray()) {
            //判断字符c是否已出现
            if (!bitSet.get(c)) {
                //未出现就设置为已出现
                bitSet.set(c);
            }
        }
        //统计有多少字符已出现过
        System.out.println(bitSet.cardinality());
    }
}
```

11. 数字颠倒
```java
import java.util.*;

public class Main{
       public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        StringBuilder sb = new StringBuilder(line);
        System.out.println(sb.reverse().toString());
    }
}
```

12. 字符串翻转（同上11.）
```java
import java.util.*;

public class Main{
       public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        StringBuilder sb = new StringBuilder(line);
        System.out.println(sb.reverse().toString());
    }
}
```

13. 句子逆序
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] strs = line.split(" ");

        StringBuilder sb = new StringBuilder();
        for (int i = strs.length - 1; i >= 0; i--) {
            sb.append(strs[i] + " ");
        }
        System.out.println(sb.toString());
    }
}
```

14. 字符串排序（字符串按照字典顺序排序）
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int count = sc.nextInt();
            String[] strs = new String[count];
            
            for (int i = 0; i < count; i++) {
                String str = sc.next();
                strs[i] = str;
            }
            Arrays.sort(strs);
            for (String s : strs) {
                System.out.println(s);
            }
        }
        sc.close();
    }
}
```

15. 求int型正整数在内存中存储时1的个数（这个数转换成2进制后，输出1的个数）【TODO】
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int count = 0;
        while (num != 0) {
            count++;
            num &= num - 1;
        }
        System.out.println(count);
        sc.close();
    }
}
```

16. 购物单（动态规划）【TODO】
```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int money = sc.nextInt();
        int n = sc.nextInt();
        if(n<=0||money<=0) System.out.println(0);

        good[] Gs = new good[n+1];
        for (int i = 1; i <= n; i++) {
            int v = sc.nextInt();
            int p = sc.nextInt();
            int q = sc.nextInt();
            Gs[i] = new good(v,p,q);

            if(q>0){
                if(Gs[q].a1==0){
                    Gs[q].setA1(i);
                }else {
                    Gs[q].setA2(i);
                }
            }
        }

        int[][] dp = new int[n+1][money+1];
        for (int i = 1; i <= n; i++) {
            int v=0,v1=0,v2=0,v3=0,tempdp=0,tempdp1=0,tempdp2=0,tempdp3=0;

            v = Gs[i].v;

            tempdp = Gs[i].p*v; //只有主件

            if(Gs[i].a1!=0){//主件加附件1
                v1 = Gs[Gs[i].a1].v+v;
                tempdp1 = tempdp + Gs[Gs[i].a1].v*Gs[Gs[i].a1].p;
            }

            if(Gs[i].a2!=0){//主件加附件2
                v2 = Gs[Gs[i].a2].v+v;
                tempdp2 = tempdp + Gs[Gs[i].a2].v*Gs[Gs[i].a2].p;
            }

            if(Gs[i].a1!=0&&Gs[i].a2!=0){//主件加附件1和附件2
                v3 = Gs[Gs[i].a1].v+Gs[Gs[i].a2].v+v;
                tempdp3 = tempdp + Gs[Gs[i].a1].v*Gs[Gs[i].a1].p + Gs[Gs[i].a2].v*Gs[Gs[i].a2].p;
            }

            for(int j=1; j<=money; j++){
                if(Gs[i].q > 0) {   //当物品i是附件时,相当于跳过
                    dp[i][j] = dp[i-1][j];
                } else {
                    dp[i][j] = dp[i-1][j];
                    if(j>=v&&v!=0) dp[i][j] = Math.max(dp[i][j],dp[i-1][j-v]+tempdp);
                    if(j>=v1&&v1!=0) dp[i][j] = Math.max(dp[i][j],dp[i-1][j-v1]+tempdp1);
                    if(j>=v2&&v2!=0) dp[i][j] = Math.max(dp[i][j],dp[i-1][j-v2]+tempdp2);
                    if(j>=v3&&v3!=0) dp[i][j] = Math.max(dp[i][j],dp[i-1][j-v3]+tempdp3);
                }
            }
        }
        System.out.println(dp[n][money]);


    }
    /**
     * 定义物品类
     */
    private static class good{
        public int v;  //物品的价格
        public int p;  //物品的重要度
        public int q;  //物品的主附件ID

        public int a1=0;   //附件1ID
        public int a2=0;   //附件2ID

        public good(int v, int p, int q) {
            this.v = v;
            this.p = p;
            this.q = q;
        }

        public void setA1(int a1) {
            this.a1 = a1;
        }

        public void setA2(int a2) {
            this.a2 = a2;
        }
    }
}
```

17. 坐标移动【TODO】
```java
import java.util.*;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        while(scanner.hasNext()) {
            String s = scanner.nextLine();
            int x = 0 , y = 0;
            String[] sArray = s.split(";");
            String res = "[ADWS]\\d{1}\\d?";
            for(int i = 0;i < sArray.length;i ++) {
                if(sArray[i].matches(res))
                    map.put(sArray[i].charAt(0),map.getOrDefault(sArray[i].charAt(0), 0)+Integer.valueOf(sArray[i].substring(1)));
            }
            x = x - map.get('A') + map.get('D');
            y = y - map.get('S') + map.get('W');
            System.out.println(x+","+y);
            map.clear();
        }
        scanner.close();
    }
}
```

18. 识别有效IP地址和掩码并进行分类统计【TODO】
```java
import java.util.*;

public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int na = 0, nb = 0, nc = 0, nd = 0, ne = 0, err = 0, pri = 0;
        String[] address; 
        String[] ip;
        String[] mask;
        while(sc.hasNext()){
            String s = sc.nextLine();
            address = s.split("~"); 
            ip = address[0].split("\\.");
            mask = address[1].split("\\.");
            if(checkMask(mask)){//掩码合法
                if(checkIp(ip)){//IP合法
                    //分类
                    int i = Integer.parseInt(ip[0]);
                    if(i >= 1 && i <= 126){
                        na++;
                        if(i == 10) pri++;
                    }
                    if(i >= 128 && i <= 191){
                        nb++;
                        if(i == 172 && Integer.parseInt(ip[1]) >= 16 && Integer.parseInt(ip[1]) <= 31) pri++;
                    }
                    if(i >= 192 && i <= 223){
                        nc++;
                        if(i == 192 && Integer.parseInt(ip[1]) == 168) pri++;
                    }
                    if(i >= 224 && i <= 239){
                        nd++;
                    }
                    if(i >= 240 && i <= 255){
                        ne++;
                    }
                }
                else{//IP不合法
                    err++;
                }
            }
            else{//掩码不合法
                err++;
            }
        }
        sc.close();
        System.out.println(na + " " + nb + " " + nc + " " + nd + " " + ne + " " + err + " " + pri);
    }
    
	private static boolean checkMask(String[] mask){
        if(mask.length != 4) return false;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < mask.length; i++){
            String b = Integer.toBinaryString(Integer.parseInt(mask[i]));
            while(b.length() != 8) b = "0" + b;
            sb.append(b);
        }
        int firstIndex = sb.indexOf("0");
        int lastIndex = sb.lastIndexOf("1");
        if(firstIndex < lastIndex) return false;
        return true;
    }
    
    private static boolean checkIp(String[] ip){
        if(ip.length == 4 && !ip[0].equals("") && !ip[1].equals("") && !ip[2].equals("") && !ip[3].equals(""))
            return true;
        return false;
    }
}
```

19. 简单错误记录【TODO】
```java
import java.io.*;
import java.util.*;
public class Main{
    public static void main(String[] args) throws IOException{
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        Map<String,Integer> m=new LinkedHashMap<String,Integer>();
        String tstr=null;
        while((tstr = cin.readLine()) != null && !tstr.equals("")){      //&& !tstr.equals(""))没有性能影响
            String[] str=tstr.split("\\s+");
            String fname=str[0].substring(str[0].lastIndexOf("\\")+1);
            fname=fname.substring(Math.max(fname.length()-16 ,0))+" "+str[1];  //max 最快推荐 ？：也可以 if太麻烦
            Integer tmp=m.get(fname);  //get==null较快写法
            if(tmp==null)
                m.put(fname,1);
            else
                m.put(fname, tmp+1);
        }
        int cnt=0;
        for(Map.Entry<String,Integer> it:m.entrySet()){
            if(m.size()-cnt<=8)
                System.out.println(it.getKey()+" "+it.getValue());
            cnt++;
        }
    }
}
```

20. 密码验证合格程序【TODO】
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// 密码验证合格程序

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        StringBuffer sb = new StringBuffer();

        while (null != (input = reader.readLine())) {

            //设置四种类型数据初始为空即false，有数据了就更改为true
            boolean[] flag = new boolean[4];
            char[] chars = input.toCharArray();

            // 第一个条件
            if (chars.length < 9) {
                sb.append("NG").append("\n");
                continue;
            }

            // 第二个条件
            for (int i = 0; i < chars.length; i++) {
                if ('A' <= chars[i] && chars[i] <= 'Z') {
                    flag[0] = true;
                } else if ('a' <= chars[i] && chars[i] <= 'z') {
                    flag[1] = true;
                } else if ('0' <= chars[i] && chars[i] <= '9') {
                    flag[2] = true;
                } else {
                    flag[3] = true;
                }
            }
            int count = 0;
            for (int i = 0; i < 4; i++) {
                if (flag[i]) {
                    count++;
                }
            }

            // 第三个条件
            boolean sign = true;        //不存在两个大于2的子串相同，即！（i=i+3,i+1=i+4,i+2=i+5）
            for (int i = 0; i < chars.length - 5; i++) {
                for (int j = i + 3; j < chars.length - 2; j++) {
                    if (chars[i] == chars[j] && chars[i + 1] == chars[j + 1] && chars[i + 2] == chars[j + 2]) {
                        sign = false;
                    }
                }
            }

            if (count >= 3 && sign) {
                sb.append("OK").append("\n");
            } else {
                sb.append("NG").append("\n");
            }
        }
        System.out.println(sb.toString());
    }
}
```

21. 简单密码【TODO】
```java
import java.io.*;
import java.util.*;

public class Main{
    public static void main(String args[]) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String str2 = "bcdefghijklmnopqrstuvwxyza222333444555666777788899990123456789";
        String a = br.readLine();
        char[] c= a.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i< c.length; i++){
            for(int j = 0; j<str1.length(); j++){
                if(c[i] == str1.charAt(j)){
                    sb.append(str2.charAt(j));
                    break;
                }
            }
          
        }
        System.out.println(sb.toString());
    }
}
```

22. 汽水瓶【TODO】
```java
import java.io.*;

public class Main{
    public static void main(String[] args)throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        
        while((str = br.readLine())!= null){
            int g = 0;
            g = Integer.parseInt(str);
            
            if(g == 0){
                return;
            }
            
            int count = 0;
            
            while (g != 0){
                int f = g/3;
                count+=f;
                g =g%3 +f;
                
                if(g < 3){
                    break;
                }
            }
            
            if(g==2){
                count+=1;
            }
            System.out.println(count);
        }
    }
}
```

23. 删除字符串中出现最少的字符【TODO】
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String str;
		LinkedHashMap<Character, Integer> map=new LinkedHashMap<Character, Integer>();
		while (null != (str = reader.readLine())) {
			char[] array=str.toCharArray();
			for(int i=0;i<array.length;i++) {
                map.put(array[i],map.getOrDefault(array[i], 0)+1);

			}
			int min=Integer.MAX_VALUE;
			for (Character key : map.keySet()) {
				
				if(min>map.get(key)) {
					min=map.get(key);//最小的次数
				}
			}
			StringBuilder res=new StringBuilder();
			for (Character c : array) {
				if (min!=map.get(c)) {
					
					res.append(c);
				}
					
			}
			System.out.println(res.toString());
			res.delete(0, res.length()-1);
			map.clear();
			
		}

	}
}
```

24. 合唱队【TODO】
```java
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            if (str.equals("")) continue;
            int n = Integer.parseInt(str);
            int[] heights = new int[n];
            String[] str_heights = br.readLine().split(" ");
            // 当仅有一个人时，其自己组成一个合唱队，出列0人
            if (n <= 1) System.out.println(0);
            else {
                for (int i = 0; i < n; i++) heights[i] = Integer.parseInt(str_heights[i]);
                // 记录从左向右的最长递增子序列和从右向左的最长递增子序列
                int[] seq = new int[n], rev_seq = new int[n];
                int[] k = new int[n];  // 用于记录以i为终点的从左向右和从右向走的子序列元素个数
                seq[0] = heights[0];  // 初始化从左向右子序列首元素为第一个元素
                int index = 1; // 记录当前子序列的长度
                for (int i = 1; i < n; i++) {
                    if (heights[i] > seq[index-1]) {  // 当当前元素大于递增序列最后一个元素时
                        k[i] = index;  // 其左边元素个数
                        seq[index++] = heights[i];  // 更新递增序列
                    } else {  // 当当前元素位于目前维护递增序列之间时
                        // 使用二分搜索找到其所属位置
                        int l = 0, r = index - 1;
                        while (l < r) {
                            int mid = l + (r - l) / 2;
                            if (seq[mid] < heights[i]) l = mid + 1;
                            else r = mid;
                        }
                        seq[l] = heights[i];  // 将所属位置值进行替换
                        k[i] = l;  // 其左边元素个数
                    }
                }

                // 随后，再从右向左进行上述操作
                rev_seq[0] = heights[n-1];
                index = 1;
                for (int i = n - 2; i >= 0; i--) {
                    if (heights[i] > rev_seq[index-1]) {
                        k[i] += index;
                        rev_seq[index++] = heights[i];
                    } else {
                        int l = 0, r = index - 1;
                        while (l < r) {
                            int mid = l + (r - l) / 2;
                            if (rev_seq[mid] < heights[i]) l = mid + 1;
                            else r = mid;
                        }
                        rev_seq[l] = heights[i];
                        k[i] += l;
                    }
                }

                int max = 1;
                for (int num: k)
                    if (max < num) max = num;
                // max+1为最大的k
                System.out.println(n - max - 1);
            }
        }
    }
}
```

25. 数据分类处理【TODO】
```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
public class Main { 
    public static void main(String[] args) throws Exception{ 
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        int inputNum = 0;
        while((str = reader.readLine()) != null) { 
            if("".equals(str)) { 
                continue;
            }
           String iText = str;
        String rText = reader.readLine();
        String[] iArr = iText.split(" ");
        String[] rArr = rText.split(" ");
        long[] rLArr = new long[Integer.parseInt(rArr[0])];
        for(int i=0; i<rLArr.length; i++) { 
            rLArr[i] = Long.parseLong(rArr[i + 1]);
        }
        Arrays.sort(rLArr);
        StringBuilder front = new StringBuilder();
        int count = 0;
        for(int i=0; i<rLArr.length; i++) { 
            int num = 0;
            if(i > 0 && rLArr[i] == rLArr[i- 1]) { 
                continue;
            }
            String pattern = rLArr[i] + "";
            StringBuilder sb = new StringBuilder();
            for(int j=1;j<iArr.length;j++) { 
                if(iArr[j].indexOf(pattern) != -1) { 
                    num ++;
                    sb.append(" ").append(j - 1).append(" ").append(iArr[j]);
                }
            }
            if(num > 0) { 
                count += num * 2 + 2;
	front.append(" ").append(rLArr[i]).append(" ").append(num).append(sb);
            }
        }
        System.out.println(count + front.toString());
        }  
    }
}
```

26. 字符串排序【TODO】
（英文字母从 A 到 Z 排列，不区分大小写；同一个英文字母的大小写同时存在时，按照输入顺序排列；非英文字母的其它字符保持原来的位置。）
```java
import java.io.*;
import java.util.*;
public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = br.readLine()) != null) {
            char[] arr = line.toCharArray();
            char[] temp = new char[arr.length];
            int offset = 0;
            for(char i = 'A'; i <= 'Z'; i++) {
                for(int j = 0; j < arr.length; j++) {
                    if(arr[j] == i || arr[j] - (char)32 == i) {
                        temp[offset++] = arr[j];
                    }
                }
            }
            offset = 0;
            for(int i = 0; i < arr.length; i++) {
                if((arr[i] >= 'A' && arr[i] <= 'Z') || (arr[i] >= 'a' && arr[i] <= 'z')) {
                    arr[i] = temp[offset++];
                }
            }
            System.out.println(String.valueOf(arr));
        }
    }
}
```

27. 查找兄弟单词【TODO】
```java
import java.util.*;
import java.io.*;
public class Main{
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while((str = br.readLine()) != null){
            if(str.equals("")){
                continue;
            }
            String[] in = str.split(" ");
            int num = Integer.parseInt(in[0]);
            String[] dict = new String[num];
            for(int i=0; i<num; i++){
                dict[i] = in[i+1];
            }
            
            String word = in[in.length-2];
            int index = Integer.parseInt(in[in.length-1]);
            
            ArrayList<String> broWord = new ArrayList<>();
            for(int i=0; i<num; i++){
                if(isBrother(dict[i], word)){
                    broWord.add(dict[i]);
                }
            }
            
            System.out.println(broWord.size());
            Collections.sort(broWord);
            
            if(index <= broWord.size()){
                System.out.println(broWord.get(index-1));
            }
        }
    }
    
    public static boolean isBrother(String str1, String str2){
        if(str1.length() != str2.length() || str1.equals(str2)){
            return false;
        }
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        
        int[] hash = new int[26];
        for(int i=0; i<ch1.length; i++){
            hash[ch1[i] - 'a']++;
            hash[ch2[i] - 'a']--;
        }
        
        for(int i=0; i<26; i++){
            if(hash[i] != 0){
                return false;
            }
        }
        return true;
    }
}
```

28. 素数伴侣【TODO】
```java
import java.util.*;
 
public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while(sc.hasNext()) {
            int n=sc.nextInt();
            ArrayList<Integer> ji =new ArrayList<>();//存放奇数
            ArrayList<Integer> ou =new ArrayList<>();//存放偶数
            for(int i=0;i<n;i++){
                int x=sc.nextInt();
                if(x%2==0)
                    ou.add(x);
                else
                    ji.add(x);
            }
            int[] used =new int[ou.size()];
            int[] oushu =new int[ou.size()];
            int sum=0;
            for(int i=0;i<ji.size();i++){
                //对每个奇数依次在所有偶数中
                Arrays.fill(used, 0);
                if(find(ji.get(i),ou,used,oushu)) sum++;
            }
            System.out.println(sum);
        }
    }
    private static boolean find(Integer x, ArrayList<Integer> ou, int[] used, int[] oushu) {
        for (int j=0;j<ou.size();j++){//扫描每个偶数
            if (isprim(x+ou.get(j)) && used[j]==0)
            {
                used[j]=1;
                if (oushu[j]==0 || find(oushu[j],ou,used,oushu)) {
                    oushu[j]=x;//索引为j的偶数对应的奇数
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isprim(Integer x) {
        int sum=0;
        for(int i=2;i<=Math.pow(x, 0.5);i++){
            if(x%i==0) return false;
        }
        return true;
    }
}
```

29. 字符串加解密【TODO】
```java
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
//给定一个字符串数组。按照字典顺序进行从小到大的排序。
 
 
public class Main {
    public static void main(String[]args)throws IOException{
        BufferedReader bt = new BufferedReader(new InputStreamReader(System.in));
        String str ="";
        while((str=bt.readLine())!=null) {
            String res = Encrypt(str);
            System.out.println(res);
            str=bt.readLine();
            String res2 = unEncrypt(str);
            System.out.println(res2);
        }
    }
    public static String Encrypt(String str) {
        char[] ch=str.toCharArray();
        char[] newch = new char[ch.length];
        for(int i=0; i<ch.length;i++) {
            char c = ch[i];
            if(c>='a'&& c<'z')
                newch[i]=(char)(c-31);
            if(c=='z')
                newch[i]='A';
            if(c>='A'&&c<'Z')
                newch[i]=(char)(c+33);//转换成小写
            if(c=='Z')
                newch[i]='a';
            if(c>='0'&&c<'9')
                newch[i]=(char)(c+1);
            if(c=='9')
                newch[i]='0';
             
    }
        return String.valueOf(newch);
     
}
    public static String unEncrypt(String str1) {
        char[] ch=str1.toCharArray();
        char[] newch = new char[ch.length];
        for(int i=0; i<ch.length;i++) {
            char c = ch[i];
            if(c>'a'&& c<='z')
                newch[i]=(char)(c-33);
            if(c=='a')
                newch[i]='Z';
            if(c>'A'&&c<='Z')
                newch[i]=(char)(c+31);//
            if(c=='A')
                newch[i]='z';
            if(c>'0'&&c<='9')
                newch[i]=(char)(c-1);
            if(c=='0')
                newch[i]='9';
             
    }
        return String.valueOf(newch);
     
    }
}
```

30. 字符串合并处理【TODO】
- 题目描述
    - 按照指定规则对输入的字符串进行处理。
- 详细描述：
    - 将输入的两个字符串合并。
    - 对合并后的字符串进行排序，要求为：下标为奇数的字符和下标为偶数的字符分别从小到大排序。这里的下标意思是字符在字符串中的位置。
    - 对排序后的字符串进行操作，如果字符为‘0’——‘9’或者‘A’——‘F’或者‘a’——‘f’，则对他们所代表的16进制的数进行BIT倒序的操作，并转换为相应的大写字符。如字符为‘4’，为0100b，则翻转后为0010b，也就是2。转换后的字符为‘2’； 如字符为‘7’，为0111b，则翻转后为1110b，也就是e。转换后的字符为大写‘E’。
    - 举例：输入str1为"dec"，str2为"fab"，合并为“decfab”，分别对“dca”和“efb”进行排序，排序后为“abcedf”，转换后为“5D37BF”
    - 注意本题含有多组样例输入
- 输入描述:
    - 本题含有多组样例输入。每组样例输入两个字符串，用空格隔开。
- 输出描述:
    - 输出转化后的结果。每组样例输出一行。
```java
import java.util.*;
import java.io.*;

public class Main{
    public static void main(String[] args) throws IOException{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while((s = bf.readLine())!=null){
            String[] str = s.split(" ");
            s = str[0] + str[1];
            char[] array = sort(s);
            System.out.println(transform(array));    
        }
    }
    
    public static char[] sort(String s){
        char[] array = s.toCharArray();
        int i,j;
        for(i=2;i<array.length;i+=2){
            if(array[i] < array[i-2]){
                char tmp = array[i];
                for(j=i;j>0 && array[j-2] > tmp; j-=2){
                    array[j] = array[j-2];
                }
                array[j] = tmp;
            }
        }
        for(i=3;i<array.length;i+=2){
            if(array[i] < array[i-2]){
                char tmp = array[i];
                for(j=i;j>1 && array[j-2]>tmp;j-=2){
                    array[j] = array[j-2];
                }
                array[j] = tmp;
            }
        }
        return array;
    }

    public static String transform(char[] array){
        for(int i=0;i<array.length;i++){
            int num = -1;
            if(array[i] >= 'A' && array[i] <= 'F'){
                num = array[i]-'A'+10;
            }else if(array[i] >= 'a' && array[i] <= 'f'){
                num = array[i]-'a'+10;
            }else if(array[i] >= '0' && array[i] <= '9'){
                num = array[i]-'0';
            }

            if(num != -1){ // 需要转换
                num = (num&1)*8 + (num&2)*2 + (num&4)/2 + (num&8)/8;
                if(num<10){
                    array[i] = (char)(num+'0');
                }else if(num<16){
                    array[i] = (char)(num-10+'A');
                }
            }
        }
        return new String(array);
    }
}
```

31. 单词倒排（类似13.句子逆序）
- 题目描述
  - 对字符串中的所有单词进行倒排。
- 说明：
  - 1、构成单词的字符只有26个大写或小写英文字母；
  - 2、非构成单词的字符均视为单词间隔符；
  - 3、要求倒排后的单词间隔符以一个空格表示；如果原字符串中相邻单词间有多个间隔符时，倒排转换后也只允许出现一个空格间隔符；
  - 4、每个单词最长20个字母；
- 输入描述:
  - 输入一行以空格来分隔的句子
- 输出描述:
  - 输出句子的逆序
```java
import java.util.*;

public class Main {

    public Main() {
    }

    public String reverse(String str) {
        // 匹配非字母的字符进行分割
        String[] words = str.split("[^A-Za-z]");
        StringBuilder result = new StringBuilder();

        // 逆序添加分割完的单词
        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]).append(" ");
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        Main solution = new Main();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String res = solution.reverse(str);
            System.out.println(res);
        }
    } 
}
```
```java
//速度快
import java.io.*;
public class Main{
    public static void main(String[] args) throws Exception {
        InputStream in = System.in;
        int available = in.available();
        char[] arr = new char[available];
        int off_word = 0;
        int off_end = arr.length;
        char c;
        for(int i = 0; i < available; i++) {
            c = (char) in.read();
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                arr[off_word++] = c;
            } else if(off_word > 0) {
                System.arraycopy(arr, 0, arr, off_end - off_word, off_word);
                off_end -= off_word + 1;
                off_word = 0;
                arr[off_end] = ' ';
            }
        }
        System.out.println(String.valueOf(arr, off_end + 1, arr.length - off_end - 1));
    }
}
```

32. 密码截取
- 题目描述
  - Catcher是MCA国的情报员，他工作时发现敌国会用一些对称的密码进行通信，比如像这些ABBA，ABA，A，123321，但是他们有时会在开始或结束时加入一些无关的字符以防止别国破解。比如进行下列变化 ABBA->12ABBA,ABA->ABAKK,123321->51233214　。因为截获的串太长了，而且存在多种可能的情况（abaaab可看作是aba,或baaab的加密形式），Cathcer的工作量实在是太大了，他只能向电脑高手求助，你能帮Catcher找出最长的有效密码串吗？
- 输入描述:
    - 输入一个字符串
- 输出描述:
    - 返回有效密码串的最大长度
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * 以某个元素为中心，向两边扩展，分别计算
 * 偶数长度的回文最大长度和奇数长度的回文最大长度。
 * 时间复杂度O(n^2)
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        while((s= bf.readLine())!=null){
            int max = 0;
            char[] arr = s.toCharArray();
            for(int i=1;i<arr.length;i++){
                //寻找以i-1,i为中点偶数长度的回文
                int low =i-1,high=i;
                while (low>0&&high<arr.length&&arr[low]==arr[high]){
                    if(high-low+1>max){
                        max=high-low+1;
                    }
                    low--;
                    high++;
                }

                //寻找以i为中心的奇数长度的回文
                low=i-1;
                high=i+1;
                while (low>=0&&high<arr.length&&arr[low]==arr[high]){
                    if(high-low+1>max){
                        max = high-low+1;
                    }
                    low--;
                    high++;
                }
            }
            System.out.println(max);
        }

    }
}

```

33. 整数与IP地址间的转换
- 题目描述
```
    原理：ip地址的每段可以看成是一个0-255的整数，把每段拆分成一个二进制形式组合起来，然后把这个二进制数转变成
    一个长整数。
    举例：一个ip地址为10.0.3.193
    每段数字             相对应的二进制数
    10                   00001010
    0                    00000000
    3                    00000011
    193                  11000001
    
    组合起来即为：00001010 00000000 00000011 11000001,转换为10进制数就是：167773121，即该IP地址转换后的数字就是它了。
本题含有多组输入用例，每组用例需要你将一个ip地址转换为整数、将一个整数转换为ip地址。
输入描述:
    输入 
    1 输入IP地址
    2 输入10进制型的IP地址

输出描述:
    输出
    1 输出转换成10进制的IP地址
    2 输出转换后的IP地址
```
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
    
public class Main{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        while ((str = br.readLine()) != null) {
            String[] ip = str.split("\\.");
            long num = Long.parseLong(br.readLine());
            //转10进制
            System.out.println(Long.parseLong(ip[0]) << 24 | Long.parseLong(ip[1]) << 16 |
                               Long.parseLong(ip[2]) << 8 | Long.parseLong(ip[3]));
            //转ip地址
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf((num >> 24) & 255)).append(".").append(String.valueOf((num >> 16) & 255))
                .append(".").append(String.valueOf((num >> 8) & 255)).append(".").append(String.valueOf(num & 255));
            System.out.println(sb.toString());
        }
    }
}

```

34. 图片整理
- 题目描述
    - Lily上课时使用字母数字图片教小朋友们学习英语单词，每次都需要把这些图片按照大小（ASCII码值从小到大）排列收好。请大家给Lily帮忙，通过C语言解决。
- 输入描述:
    - Lily使用的图片包括"A"到"Z"、"a"到"z"、"0"到"9"。输入字母或数字个数不超过1024。
- 输出描述:
    - Lily的所有图片按照从小到大的顺序输出
```java
import java.util.*;
import java.io.*;
public class Main{
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = "";
        while((s=br.readLine())!=null){
            char[] result = s.toCharArray();
            Arrays.sort(result);
            System.out.println(String.valueOf(result));
        }
    }
}
```

35. 蛇形矩阵
- 题目描述
```
蛇形矩阵是由1开始的自然数依次排列成的一个矩阵上三角形。
例如，当输入5时，应该输出的三角形为：
1 3 6 10 15
2 5 9 14
4 8 13
7 12
11
输入描述:
    输入正整数N（N不大于100）
输出描述:
    输出一个N行的蛇形矩阵。
```
```java
import java.io.*;
public class Main{
    public static void main(String[] args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while((str = br.readLine())!=null){
            int num = Integer.parseInt(str);
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i <= num; i++){
                int start = (i - 1) * i / 2 + 1;
                int step = i + 1;
                for(int j = 1; j <= num - i + 1; j++){
                    sb.append(start).append(" ");
                    start += step;
                    step ++ ;
                }
                sb.setCharAt(sb.length()-1,'\n');
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
        }
    }
}
```

36. 字符串加密
```
题目描述
有一种技巧可以对数据进行加密，它使用一个单词作为它的密匙。下面是它的工作原理：首先，选择一个单词作为密匙，如TRAILBLAZERS。如果单词中包含有重复的字母，只保留第1个，其余几个丢弃。现在，修改过的那个单词属于字母表的下面，如下所示：
A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
T R A I L B Z E S C D F G H J K M N O P Q U V W X Y
上面其他用字母表中剩余的字母填充完整。在对信息进行加密时，信息中的每个字母被固定于顶上那行，并用下面那行的对应字母一一取代原文的字母(字母字符的大小写状态应该保留)。因此，使用这个密匙，Attack AT DAWN(黎明时攻击)就会被加密为Tpptad TP ITVH。
请实现下述接口，通过指定的密匙和明文得到密文。
输入描述:
    先输入key和要加密的字符串
输出描述:
    返回加密后的字符串
```
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
 
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String key;
        while ((key = br.readLine()) != null) {
            char[] chars = key.toLowerCase().toCharArray();
            char[] dict = new char[26];
            int index = 0;
            tag1:
            for (char ch : chars) {
                for (int i = 0; i < index; i++) {
                    if (ch == dict[i]) {
                        continue tag1;
                    }
                }
                dict[index] = ch;
                index++;
            }
 
            char ch = 'a';
            tag2:
            for (int i = 0; i < 26; i++) {
                for (int j = 0; j < index; j++) {
                    if (dict[j] == ch) {
                        ch++;
                        continue tag2;
                    }
                }
                dict[index] = ch;
                ch++;
                index++;
            }
 
            String str = br.readLine();
            char[] res = str.toCharArray();
            for (int i = 0; i < res.length; i++) {
                if(res[i] - 'a'>=0){
                    res[i] = dict[res[i] - 'a'];
                }else{
                    res[i] = dict[res[i] - 'A'];
                }
                
            }
 
            System.out.println(String.valueOf(res));
        }
    }
}
```