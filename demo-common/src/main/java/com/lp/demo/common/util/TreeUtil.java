package com.lp.demo.common.util;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2025/4/27 20:10
 * @desc
 **/
public class TreeUtil {

    public static void main(String[] args) {

        // 平铺结构的权限列表
        List<PermissionVO> flatPermissions = new ArrayList<>();
        for (long i = 100; i < 110; i++) {
            PermissionVO permissionVO = new PermissionVO();
            permissionVO.setId(i);
            permissionVO.setName("name-" + i);
            if (i % 3 == 0) {
                permissionVO.setParentId(i - 1);
            }
            flatPermissions.add(permissionVO);
        }


        Map<Long, List<PermissionVO>> parentMap = flatPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getParentId() == null ? 0L : p.getParentId()
                ));

        List<PermissionVO> permissionVOS = buildPermissionTree(0L, parentMap);
        System.out.println("permissionVOS = " + JSONArray.parseArray(JSONArray.toJSONString(permissionVOS)));
    }

    // 递归构建树结构
    private static List<PermissionVO> buildPermissionTree(Long parentId, Map<Long, List<PermissionVO>> parentMap) {
        return Optional.ofNullable(parentMap.get(parentId))
                .orElse(Collections.emptyList())
                .stream()
                .peek(node -> node.setChildren(buildPermissionTree(node.getId(), parentMap)))
                .collect(Collectors.toList());
    }

    @Data
    public static class PermissionVO {

        private Long id;
        private String name;
        private Integer type;
        private String url;
        private Long parentId;

        private Boolean state;
        private List<PermissionVO> children;
    }
}
