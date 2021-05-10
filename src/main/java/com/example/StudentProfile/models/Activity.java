package com.example.StudentProfile.models;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "activities")
public class Activity { // название, расстояние, время, количество, сила сжатия (нбютоны), пульс, давление

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private Double time;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "power")
    private String Double;

    @Column(name = "pulse")
    private Double pulse;

    @Column(name = "pressure")
    private String pressure;

    @ManyToOne()
    @JoinColumn(name ="session_id", nullable=false)
    private Session session;

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.lang.Double getTime() {
        return time;
    }

    public void setTime(java.lang.Double time) {
        this.time = time;
    }

    public java.lang.Double getDistance() {
        return distance;
    }

    public void setDistance(java.lang.Double distance) {
        this.distance = distance;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPulse() {
        return pulse;
    }

    public void setPulse(Double pulse) {
        this.pulse = pulse;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

//    public Session getSession() {
//        return session;
//    }

    public void setSession(Session session) {
        this.session = session;
    }
}
