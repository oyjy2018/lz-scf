package com.zhjs.scfcloud.util.util;

import java.util.*;

/**
 * 数组工具类
 * <功能详细描述>
 * Version: 1.0.0, 2019-06-24 10:16
 *
 * @author liuchanghai
 * @create 2019-06-24 10:16
 * @since
 */
public class ArraysUtil {

    /**
     * String 转 List
     * @param strings
     * @param regex
     * @return
     */
    public static List<Long> stringToList(String strings, String regex){
        List<Long> ids = new ArrayList<>();
        String[] splits = strings.split(regex);
        for (String s: splits){
            ids.add(Long.parseLong(s));
        }
        return ids;
    }

    public static String listToString(List list, String regex){
        String string = null;
//        for (list){
//            ids.add(Long.parseLong(s));
//        }
        return string;
    }

    public static void main(String[] args) {
//        System.out.println(toUnderlineName("nameUserName12"));
        List<String> fileds = new ArrayList<>();
        fileds.add("creditApply_ecCooperationYear");
        fileds.add("creditItem_applyPurpose");
        fileds.add("creditRisk_riskCredit14");
        fileds.add("creditItem_applyPurpose");
        fileds.add("creditAuditData_hasBlocDebt");
        fileds.add("creditItem_applyPurpose");

        Map<String, String> table = new HashMap();
        Map<String, String> value = new HashMap();
        Map<String, String> valueCH = new HashMap();
        for (String filed: fileds){
            String[] split = filed.split("_");
            String tbl = StringUtil.toUnderlineName(split[0]);
            String fied = StringUtil.toUnderlineName(split[1]);
            String val = table.get(tbl);
            if(StringUtil.isEmpty(val)){
                table.put(tbl,fied);
            }else {
                String newValue = val + "," + fied;
                table.put(tbl,newValue);
            }
        }
//        //方式二
//        Set<String> set2 = new HashSet<>();
//        Collections.addAll(set2, strs);
//        System.out.println(set2.toString());
//
//        String join = String.join(",", set2);
        System.out.println(table);
        System.out.println(table.keySet());
        for (String tbl: table.keySet()){
            System.out.println(table.get(tbl));
        }

        List<Long> permissionOrgIds = new ArrayList<>();
        String[] splits = "1,2,3,4,5,6".split(",");
        for (String s: splits){
            permissionOrgIds.add(Long.parseLong(s));
        }
        System.out.println(permissionOrgIds.toString());
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");
        set.add("5");
        String pIds = String.join(",",set);
        System.out.println(pIds);

//        String url = "1/2/222/122132656.jsp";
//        String fileName = url.substring(url.lastIndexOf("/") + 1);
//        System.out.println(fileName);
    }
}
