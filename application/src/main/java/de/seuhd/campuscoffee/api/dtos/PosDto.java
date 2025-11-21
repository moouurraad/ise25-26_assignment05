package de.seuhd.campuscoffee.api.dtos;

import de.seuhd.campuscoffee.domain.model.CampusType;
import de.seuhd.campuscoffee.domain.model.PosType;

public class PosDto {
    private Long id;
    private String name;
    private String description;
    private PosType type;
    private CampusType campus;
    private String street;
    private String houseNumber;
    private int postalCode;
    private String city;

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public PosType getType() { return type; }
    public void setType(PosType type) { this.type = type; }

    public CampusType getCampus() { return campus; }
    public void setCampus(CampusType campus) { this.campus = campus; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public int getPostalCode() { return postalCode; }
    public void setPostalCode(int postalCode) { this.postalCode = postalCode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

  
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final PosDto dto = new PosDto();

        public Builder name(String name) { dto.setName(name); return this; }
        public Builder description(String description) { dto.setDescription(description); return this; }
        public Builder type(PosType type) { dto.setType(type); return this; }
        public Builder campus(CampusType campus) { dto.setCampus(campus); return this; }
        public Builder street(String street) { dto.setStreet(street); return this; }
        public Builder houseNumber(String houseNumber) { dto.setHouseNumber(houseNumber); return this; }
        public Builder postalCode(int postalCode) { dto.setPostalCode(postalCode); return this; }
        public Builder city(String city) { dto.setCity(city); return this; }
        public PosDto build() { return dto; }
    }
}
