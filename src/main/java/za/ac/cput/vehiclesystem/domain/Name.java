package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Name {

    private String firstName;
    private String middleName;
    private String lastName;

    protected Name() {}

    protected Name(Builder builder) {
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String toString() {
        return "Name{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public static class Builder {
        private String firstName;
        private String middleName;
        private String lastName;

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public static Builder copy(Name name) {
            Builder builder = new Builder();
            builder.firstName = name.firstName;
            builder.middleName = name.middleName;
            builder.lastName = name.lastName;
           return builder;
        }

        public Name build() {
            return new Name(this);
        }
    }
}