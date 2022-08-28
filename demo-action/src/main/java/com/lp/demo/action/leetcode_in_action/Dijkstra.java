package com.lp.demo.action.leetcode_in_action;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author lp
 * @create 2021/5/3 18:15
 * @description Shortest path
 **/
public class Dijkstra {


    public static void main(String[] args) {
//        String startCity = "atlanta";
//        String[] otherCity = {"boston", "chicago", "denver", "el_paso"};
//        String[] routes = dijkstra(startCity, otherCity);
//        Arrays.asList(routes).forEach();
//        each do |city, price_info|
//                p "#{city.name}: #{price_info[0]}"
//        end

        int MAX = Integer.MAX_VALUE - 10000;
        int[][] weight = {
                {0, 1, 12, MAX, MAX, MAX},
                {MAX, 0, 9, 3, MAX, MAX},
                {MAX, MAX, 0, MAX, 5, MAX},
                {4, 0, 13, 15, MAX, MAX},
                {MAX, MAX, MAX, MAX, 0, 4},
                {MAX, MAX, MAX, MAX, MAX, 0}
        };
        int start = 0;  //选择出发点
        int[] sp = getShortPath(weight, start);
        System.out.println(Arrays.toString(sp));


    }





    public static int[] getShortPath(int[][] weight, int start) {
        // 定义一个数组保存各顶点是否被访问过，初始化为0,。因为我们从0号节点出发，设置0号顶点为已访问。
        int[] visit = new int[weight.length]; //标记某节点是否被访问过
        for (int i : visit) { //初始标记为未访问
            visit[i] = 0;
            visit[start] = 1;
        }

        // 在循环中逐个访问每个顶点，并不断修正权重。
        for (int k = 1; k <= weight.length - 1; k++) {
            //循环中每次确定一个新的确定最短路径的节点
            //TODO
        }

        // 选择一个未标记的且离出发点最近的顶点。
        int dmin = Integer.MAX_VALUE;
        int position = 0;
        //找出一个未标记的离出发点最近的节点
        for (int i = 0; i < weight.length; i++) {
            if (visit[i] == 0 && weight[start][i] < dmin && i != start) {
                dmin = weight[start][i];
                position = i;
            }
        }
        System.out.println("选出一个最短的路径：" + dmin + "它是位置是：" + position);
        //标记该节点为已经访问过
        visit[position] = 1;

        // 修正从出发顶点经由此顶点，再到其他顶点的最短路径。
        for (int i = 1; i < weight.length; i++) {
            if (weight[start][position] + weight[position][i] < weight[start][i] && i != position) {
                System.out.println("原先的最短路径到" + i + "距离是:" + weight[start][i]);
                System.out.println("两者分别是" + weight[start][position] + "," + weight[position][i]);
                weight[start][i] = weight[position][i] + weight[start][position]; //更新最短路径
                System.out.println("更新最短路径到" + i + "距离是:" + weight[start][i]);
            }
        }

        // 循环直到所有顶点被访问完。最后获取出发顶点到各个顶点的最短路径数组并返回。
        int[] shortPath = new int[weight.length];
        for (int i = 0; i < weight.length; i++) {
            shortPath[i] = weight[0][i];
        }
        return shortPath;
    }


    static class City {
        String name;
        HashMap routes;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public City() {
        }

//        public City(String name, HashMap routes) {
//            this.name = name;
//            this.routes = routes;
//        }

        public void addRoute(String cityName, int priceInfo) {
            routes.put(cityName, priceInfo);
        }
    }

    public Dijkstra() {
        String[] allCity = {"atlanta", "boston", "chicago", "denver", "el_paso"};
        Arrays.asList(allCity).stream().forEach(c -> {
            new City().setName(c);
        });
//        City city = new City();
//        city.setName("atlanta");
//        String atlanta = city.getName();
//        city.addRoute(atlanta, 100);
//
//        new City().setName("atlanta");addRoute("atlanta", 100);
//        new City("boston");
//        new City("chicago");
//        new City("denver");
//        new City("el_paso");
    }

//    public static void init() {
//        String[] allCity = {"atlanta", "boston", "chicago", "denver", "el_paso"};
//        Arrays.asList(allCity).stream().forEach(c -> {
//            new City().setName(c);
//        });
//    }
/*
    public static String[] dijkstra(String starting_city, String[] other_cities) {
        // 散列表routes_from_city 用来保存从给定城市到其他所有城市的价格，以及途经的城市
        Map<String, Integer> routes_from_city = new HashMap();
        // 它的格式如下：
        // {终点城市 => [价格, 到达终点城市前所要经过的那个城市]}
        // 以上图为例，此散列表最后会是：
        // {atlanta => [0, nil], boston => [100, atlanta], chicago => [200, denver],
        // denver => [160, atlanta], el_paso => [280, chicago]}
        // 从起点城市到起点城市是免费的
//        routes_from_city[starting_city] = [0, staring_city]
        routes_from_city.put(starting_city, 0);

        // 初始化该散列表时，因为去往所有其他城市的花费都未知，所以先设为无限
        Arrays.asList(other_cities).forEach(c -> {
            routes_from_city.put(c, Integer.MAX_VALUE);
        });

        // 以上图为例，此散列表起初会是：
        // {atlanta => [0, nil], boston => [Float::INFINITY, nil],
        // chicago => [Float::INFINITY, nil],
        // denver => [Float::INFINITY, nil], el_paso => [Float::INFINITY, nil]}
        // 已访问的城市记录在这个数组里
        String[] visited_cities = new String[Integer.MAX_VALUE];
        // 一开始先访问起点城市，将current_city 设为它
        String current_city = starting_city;
        // 进入算法的核心逻辑，循环访问每个城市
        while (current_city != null && current_city.length() > 0) {
            // 正式访问当前城市
            int i = 0;
            visited_cities[i] = current_city;
            // 检查从当前城市出发的每条航线
            current_city.routes.each do |city, price_info |
            // 如果起点城市到其他城市的价格比routes_from_city 所记录的更低，
            // 则更新记录
            if routes_from_city[city][0] > price_info +
                    routes_from_city[current_city][0]
            routes_from_city[city] = [price_info + routes_from_city[current_city][0], current_city]
            end
                    end
            // 决定下一个要访问的城市
            current_city = nil
            cheapest_route_from_current_city = Float::INFINITY
            // 检查所有已记录的路线
            routes_from_city.each do |city, price_info |
            // 在未访问的城市中找出最便宜的那个，
            // 设为下一个要访问的城市
            if price_info[0] < cheapest_route_from_current_city && !visited_cities.include ? (city)
                    cheapest_route_from_current_city = price_info[0]
            current_city = city
            end
                    end
        }
        return routes_from_city
        end
    }*/
}
