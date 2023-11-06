package main.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
class Activity
{
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    private String description;

    Activity()
    {

    }

    Activity(Long id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    Activity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Activity activity))
            return false;
        return Objects.equals(this.id, activity.id) &&
               Objects.equals(this.name, activity.name) &&
               Objects.equals(this.description, activity.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.name, this.description);
    }

    @Override
    public String toString()
    {
        return "Activity{" + "id=" + this.id + ", name='" + this.name + '\'' + ", description='" + this.description + '\'' + '}';
    }
}
