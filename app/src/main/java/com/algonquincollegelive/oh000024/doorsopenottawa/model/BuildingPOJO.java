package com.algonquincollegelive.oh000024.doorsopenottawa.model;

/**
 *  The purpose of class BuildingPOJO is model a building JSON object for the Doors Open Ottawa web service
 *  @author Jake Oh (oh000024@algonquinlive.com)
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuildingPOJO implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("buildingId")
    @Expose
    private Integer buildingId;
    @SerializedName("nameEN")
    @Expose
    private String nameEN;
    @SerializedName("categoryFR")
    @Expose
    private String categoryFR;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("province")
    @Expose
    private String province;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("imageDescriptionFR")
    @Expose
    private String imageDescriptionFR;
    @SerializedName("imageDescriptionEN")
    @Expose
    private String imageDescriptionEN;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sundayClose")
    @Expose
    private Object sundayClose;
    @SerializedName("sundayStart")
    @Expose
    private Object sundayStart;
    @SerializedName("saturdayClose")
    @Expose
    private String saturdayClose;
    @SerializedName("saturdayStart")
    @Expose
    private String saturdayStart;
    @SerializedName("categoryEN")
    @Expose
    private String categoryEN;
    @SerializedName("descriptionFR")
    @Expose
    private String descriptionFR;
    @SerializedName("descriptionEN")
    @Expose
    private String descriptionEN;
    @SerializedName("addressFR")
    @Expose
    private String addressFR;
    @SerializedName("addressEN")
    @Expose
    private String addressEN;
    @SerializedName("isNewBuilding")
    @Expose
    private Boolean isNewBuilding;
    @SerializedName("nameFR")
    @Expose
    private String nameFR;
    public final static Parcelable.Creator<BuildingPOJO> CREATOR = new Creator<BuildingPOJO>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BuildingPOJO createFromParcel(Parcel in) {
            return new BuildingPOJO(in);
        }

        public BuildingPOJO[] newArray(int size) {
            return (new BuildingPOJO[size]);
        }

    }
            ;

    protected BuildingPOJO(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.buildingId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.nameEN = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryFR = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.postalCode = ((String) in.readValue((String.class.getClassLoader())));
        this.province = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.imageDescriptionFR = ((String) in.readValue((String.class.getClassLoader())));
        this.imageDescriptionEN = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.sundayClose = ((Object) in.readValue((Object.class.getClassLoader())));
        this.sundayStart = ((Object) in.readValue((Object.class.getClassLoader())));
        this.saturdayClose = ((String) in.readValue((String.class.getClassLoader())));
        this.saturdayStart = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryEN = ((String) in.readValue((String.class.getClassLoader())));
        this.descriptionFR = ((String) in.readValue((String.class.getClassLoader())));
        this.descriptionEN = ((String) in.readValue((String.class.getClassLoader())));
        this.addressFR = ((String) in.readValue((String.class.getClassLoader())));
        this.addressEN = ((String) in.readValue((String.class.getClassLoader())));
        this.isNewBuilding = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.nameFR = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public BuildingPOJO() {
    }

    /**
     *
     * @param addressFR
     * @param descriptionEN
     * @param nameFR
     * @param isNewBuilding
     * @param categoryId
     * @param image
     * @param nameEN
     * @param descriptionFR
     * @param sundayClose
     * @param city
     * @param id
     * @param categoryEN
     * @param saturdayStart
     * @param postalCode
     * @param addressEN
     * @param province
     * @param imageDescriptionFR
     * @param sundayStart
     * @param categoryFR
     * @param longitude
     * @param saturdayClose
     * @param latitude
     * @param imageDescriptionEN
     * @param buildingId
     */
    public BuildingPOJO(String id, Integer buildingId, String nameEN, String categoryFR, Integer categoryId, Double longitude, Double latitude, String postalCode, String province, String city, String imageDescriptionFR, String imageDescriptionEN, String image, Object sundayClose, Object sundayStart, String saturdayClose, String saturdayStart, String categoryEN, String descriptionFR, String descriptionEN, String addressFR, String addressEN, Boolean isNewBuilding, String nameFR) {
        super();
        this.id = id;
        this.buildingId = buildingId;
        this.nameEN = nameEN;
        this.categoryFR = categoryFR;
        this.categoryId = categoryId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.postalCode = postalCode;
        this.province = province;
        this.city = city;
        this.imageDescriptionFR = imageDescriptionFR;
        this.imageDescriptionEN = imageDescriptionEN;
        this.image = image;
        this.sundayClose = sundayClose;
        this.sundayStart = sundayStart;
        this.saturdayClose = saturdayClose;
        this.saturdayStart = saturdayStart;
        this.categoryEN = categoryEN;
        this.descriptionFR = descriptionFR;
        this.descriptionEN = descriptionEN;
        this.addressFR = addressFR;
        this.addressEN = addressEN;
        this.isNewBuilding = isNewBuilding;
        this.nameFR = nameFR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BuildingPOJO withId(String id) {
        this.id = id;
        return this;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public BuildingPOJO withBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
        return this;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public BuildingPOJO withNameEN(String nameEN) {
        this.nameEN = nameEN;
        return this;
    }

    public String getCategoryFR() {
        return categoryFR;
    }

    public void setCategoryFR(String categoryFR) {
        this.categoryFR = categoryFR;
    }

    public BuildingPOJO withCategoryFR(String categoryFR) {
        this.categoryFR = categoryFR;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BuildingPOJO withCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public BuildingPOJO withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public BuildingPOJO withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public BuildingPOJO withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public BuildingPOJO withProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BuildingPOJO withCity(String city) {
        this.city = city;
        return this;
    }

    public String getImageDescriptionFR() {
        return imageDescriptionFR;
    }

    public void setImageDescriptionFR(String imageDescriptionFR) {
        this.imageDescriptionFR = imageDescriptionFR;
    }

    public BuildingPOJO withImageDescriptionFR(String imageDescriptionFR) {
        this.imageDescriptionFR = imageDescriptionFR;
        return this;
    }

    public String getImageDescriptionEN() {
        return imageDescriptionEN;
    }

    public void setImageDescriptionEN(String imageDescriptionEN) {
        this.imageDescriptionEN = imageDescriptionEN;
    }

    public BuildingPOJO withImageDescriptionEN(String imageDescriptionEN) {
        this.imageDescriptionEN = imageDescriptionEN;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BuildingPOJO withImage(String image) {
        this.image = image;
        return this;
    }

    public Object getSundayClose() {
        return sundayClose;
    }

    public void setSundayClose(Object sundayClose) {
        this.sundayClose = sundayClose;
    }

    public BuildingPOJO withSundayClose(Object sundayClose) {
        this.sundayClose = sundayClose;
        return this;
    }

    public Object getSundayStart() {
        return sundayStart;
    }

    public void setSundayStart(Object sundayStart) {
        this.sundayStart = sundayStart;
    }

    public BuildingPOJO withSundayStart(Object sundayStart) {
        this.sundayStart = sundayStart;
        return this;
    }

    public String getSaturdayClose() {
        return saturdayClose;
    }

    public void setSaturdayClose(String saturdayClose) {
        this.saturdayClose = saturdayClose;
    }

    public BuildingPOJO withSaturdayClose(String saturdayClose) {
        this.saturdayClose = saturdayClose;
        return this;
    }

    public String getSaturdayStart() {
        return saturdayStart;
    }

    public void setSaturdayStart(String saturdayStart) {
        this.saturdayStart = saturdayStart;
    }

    public BuildingPOJO withSaturdayStart(String saturdayStart) {
        this.saturdayStart = saturdayStart;
        return this;
    }

    public String getCategoryEN() {
        return categoryEN;
    }

    public void setCategoryEN(String categoryEN) {
        this.categoryEN = categoryEN;
    }

    public BuildingPOJO withCategoryEN(String categoryEN) {
        this.categoryEN = categoryEN;
        return this;
    }

    public String getDescriptionFR() {
        return descriptionFR;
    }

    public void setDescriptionFR(String descriptionFR) {
        this.descriptionFR = descriptionFR;
    }

    public BuildingPOJO withDescriptionFR(String descriptionFR) {
        this.descriptionFR = descriptionFR;
        return this;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public BuildingPOJO withDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
        return this;
    }

    public String getAddressFR() {
        return addressFR;
    }

    public void setAddressFR(String addressFR) {
        this.addressFR = addressFR;
    }

    public BuildingPOJO withAddressFR(String addressFR) {
        this.addressFR = addressFR;
        return this;
    }

    public String getAddressEN() {
        return addressEN;
    }

    public void setAddressEN(String addressEN) {
        this.addressEN = addressEN;
    }

    public BuildingPOJO withAddressEN(String addressEN) {
        this.addressEN = addressEN;
        return this;
    }

    public Boolean getIsNewBuilding() {
        return isNewBuilding;
    }

    public void setIsNewBuilding(Boolean isNewBuilding) {
        this.isNewBuilding = isNewBuilding;
    }

    public BuildingPOJO withIsNewBuilding(Boolean isNewBuilding) {
        this.isNewBuilding = isNewBuilding;
        return this;
    }

    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }

    public BuildingPOJO withNameFR(String nameFR) {
        this.nameFR = nameFR;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(buildingId);
        dest.writeValue(nameEN);
        dest.writeValue(categoryFR);
        dest.writeValue(categoryId);
        dest.writeValue(longitude);
        dest.writeValue(latitude);
        dest.writeValue(postalCode);
        dest.writeValue(province);
        dest.writeValue(city);
        dest.writeValue(imageDescriptionFR);
        dest.writeValue(imageDescriptionEN);
        dest.writeValue(image);
        dest.writeValue(sundayClose);
        dest.writeValue(sundayStart);
        dest.writeValue(saturdayClose);
        dest.writeValue(saturdayStart);
        dest.writeValue(categoryEN);
        dest.writeValue(descriptionFR);
        dest.writeValue(descriptionEN);
        dest.writeValue(addressFR);
        dest.writeValue(addressEN);
        dest.writeValue(isNewBuilding);
        dest.writeValue(nameFR);
    }

    public int describeContents() {
        return 0;
    }

}