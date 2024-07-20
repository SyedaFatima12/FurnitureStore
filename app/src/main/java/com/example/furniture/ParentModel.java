package com.example.furniture;

import java.util.List;

public class ParentModel {

    private String ParentItemTitle;
    private List<ChildModel> childModelList;

    public ParentModel(String parentItemTitle, List<ChildModel> childModelList) {
        ParentItemTitle = parentItemTitle;
        this.childModelList = childModelList;
    }

    public String getParentItemTitle() {
        return ParentItemTitle;
    }

    public void setParentItemTitle(String parentItemTitle) {
        ParentItemTitle = parentItemTitle;
    }

    public List<ChildModel> getChildModelList() {
        return childModelList;
    }

    public void setChildModelList(List<ChildModel> childModelList) {
        this.childModelList = childModelList;
    }
}
