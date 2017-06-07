package com.hzq.app.view.expandablerecycle;

import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 17/5/9
 * @version:
 * @description:
 */

public class ParentEntity {

    public String id;
    public String name;
    public List<ChildEntity> childEntities;

    public static class ChildEntity{
        public String cId;
        public String cName;
    }
}
