package za.ac.cput.vehiclesystem.domain;

import jakarta.persistence.*;

@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String agentId;
    @Embedded
    private Name name;
    private String email;
    private String mobile;

    protected Agent() {}

    protected Agent(Builder builder) {
        this.agentId = builder.agentId;
        this.name = builder.name;
        this.email = builder.email;
        this.mobile = builder.mobile;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public Name getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "agentId='" + agentId + '\'' +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public static class Builder {
        private String agentId;
        private Name name;
        private String email;
        private String mobile;

        public Builder setAgentId(String agentId) {
            this.agentId = agentId;
            return this;
        }

        public Builder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder setName(Name name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public static Builder copy(Agent agent){
            Builder builder = new Builder();
            builder.agentId = agent.agentId;
            builder.name = agent.name;
            builder.email = agent.email;
            builder.mobile = agent.mobile;
            return builder;
        }

        public Agent build() {
            return new Agent(this);
        }
    }
}
