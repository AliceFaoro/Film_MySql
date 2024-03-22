package film;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Film {

	int id;
	String titolo;
	String regista;
	String genere;
	int anno;

	public Film(String titolo, String regista, String genere, int anno) {
		this.titolo = titolo;
		this.regista = regista;
		this.genere = genere;
		this.anno = anno;
	}

	@Override
	public String toString() {
		return "titolo=" + titolo + ", regista=" + regista + ", genere=" + genere + ", anno="
				+ anno + "";
	}

	

}
