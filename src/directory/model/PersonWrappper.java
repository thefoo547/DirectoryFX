package directory.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="people")
public class PersonWrappper {
    private List<Person> people;

    @XmlElement(name="persons")
    public List<Person> getPeople(){
        return people;
    }
    public void setPeople(List<Person> people){
        this.people=people;
    }
}
