package Entiny;

import org.json.JSONObject;

public class CategoryEntiny {
    private int  categoryId;
    private String categoryName;

    public CategoryEntiny(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public CategoryEntiny(JSONObject jsonObject){
        categoryId = jsonObject.optInt("category_id");
        categoryName = jsonObject.optString("category_name");
    }

    @Override
    public String toString() {
        return "CategoryEntiny{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
