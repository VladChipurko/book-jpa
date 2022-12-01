package telran.java2022.book.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "publisherName")
@Entity
public class Publisher implements Serializable{

	private static final long serialVersionUID = 5083976659882404642L;

	@Id
	String publisherName;
	@OneToMany(mappedBy = "publisher")
	Set<Book> books;

	public Publisher(String publisherName) {
		super();
		this.publisherName = publisherName;
	}
	
}
