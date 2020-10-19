package org.github.jefesimpson.backend.service.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class VipApp {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User user;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate createdDate;

    public VipApp() {
    }

    public VipApp(int id, User user, LocalDate createdDate) {
        this.id = id;
        this.user = user;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VipApp vip = (VipApp) o;
        return id == vip.id &&
                Objects.equals(user, vip.user) &&
                Objects.equals(createdDate, vip.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, createdDate);
    }

    @Override
    public String toString() {
        return "Vip{" +
                "id=" + id +
                ", user=" + user +
                ", createdDate=" + createdDate +
                '}';
    }
}
