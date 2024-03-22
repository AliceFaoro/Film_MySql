package film;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import dipendenti.Dipendente;

public class UsaFilm {

	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3306/"; // URL del database
		String dbName = "newDB"; // Nome del database
		String user = "root"; // Nome utente
		String password = "Topolino.16"; //password

		ArrayList<Film> listaFilm = new ArrayList<Film>();
		int scelta;

		do {
			Scanner scan = new Scanner(System.in);
			System.out.println("Premi 1 per inserire un nuovo film, 2 per cercare per genere,\n3 per cercare per anno, 4 per stampare tutti i film e 0 per terminare");
			scelta = scan.nextInt();
			scan.nextLine();

			if (scelta == 1) {

				System.out.print("Inserisci titolo: ");
				String titolo = scan.nextLine();
				scan.nextLine();
				System.out.print("Inserisci registra: ");
				String regista = scan.nextLine();
				scan.nextLine();
				System.out.print("Inserisci il genere: ");
				String genere = scan.nextLine();
				scan.nextLine();
				System.out.print("Inserisci anno di uscita: ");
				int anno = scan.nextInt();
				scan.nextLine();
				
				Film f = new Film(titolo, regista, genere, anno);
				listaFilm.add(f);

				/* Viene preparata una query SQL parametrizzata per l'inserimento dei dati nel
				  database. La query è definita nella stringa insertQuery1.
				  Una query SQL parametrizzata è una query in cui i valori dei parametri non sono direttamente 
				  inclusi nella stringa SQL, ma sono rappresentati da segnaposto (di solito ?). Nella
				  stringa insertQuery1, vengono specificate le colonne della tabella film a cui
				  si vogliono inserire dati e i segnaposto per i valori da inserire.*/

				String insertQuery1 = "INSERT INTO film (titolo, regista, genere, anno) VALUES (?, ?, ?, ?)";

				/*Connessione al database: Viene stabilita la connessione al database
				  utilizzando DriverManager.getConnection(). Questo metodo richiede tre
				  parametri: 1) url: è l'URL del database a cui ci si sta connettendo.
				  2) user: è il nome utente per accedere al database. 
				  3) password: è la password associata all'utente per accedere al database.*/

				try (Connection conn = DriverManager.getConnection(url + dbName, user, password);

						/*Preparazione dello statement SQL: Viene preparato uno statement SQL
					  utilizzando conn.prepareStatement(). Questo metodo prepara l'esecuzione di
					  una query SQL. La query è già stata definita nella stringa insertQuery1.
					  Questo statement preparato permette di inserire valori dinamici nella query
					  utilizzando i segnaposto.*/

						PreparedStatement stmt = conn.prepareStatement(insertQuery1)) {

					/*Impostazione dei parametri: I valori dei parametri della query vengono
					 impostati utilizzando i metodi setString() e setInt() dello statement
					 preparato. In questo caso, stmt.setString(1, titolo) imposta il primo
					 segnaposto con il valore della variabile titolo ecc.*/

					stmt.setString(1, titolo);
					stmt.setString(2, regista);
					stmt.setString(3, genere);
					stmt.setInt(4, anno);

					/*Esecuzione della query: Viene eseguita la query per l'inserimento dei dati
					nel database utilizzando stmt.executeUpdate(). Questo metodo esegue la query
					SQL e restituisce il numero di righe modificate nel database. Nel caso
					dell'inserimento di un nuovo film, restituirà 1 se l'inserimento è avvenuto
					con successo. Il risultato può essere salvato in una variabile come rowsAffected*/
					int rowsAffected = stmt.executeUpdate();


					/*Eventuali eccezioni durante l'esecuzione della query vengono catturate e
			    gestite, stampando un messaggi di errore e le informazioni sull'eccezione.*/

				} catch (SQLException e) {

					System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
					e.printStackTrace();
				}

			} else if (scelta == 2) {

				System.out.println("inserisci il genere del film da ricercare");
				Scanner input2 = new Scanner(System.in);
				String gen = input2.nextLine();

				/*Preparazione della query SQL: Viene definita una stringa queryGen che contiene una query SQL parametrica
				per selezionare tutti i film dal database che corrispondono al genere specificato dall'utente. 
				La stringa contiene un segnaposto ? che verrà sostituito con il valore effettivo del genere durante l'esecuzione.*/

				String queryGen = "SELECT * FROM film WHERE genere = (?)";

				/*Il programma stabilisce una connessione al database utilizzando DriverManager.getConnection()
				e prepara una dichiarazione SQL utilizzando PreparedStatement. La query parametrica è stata preparata
			 	con conn.prepareStatement(queryGen).*/

				try (Connection conn = DriverManager.getConnection(url + dbName, user, password);
						PreparedStatement stmt = conn.prepareStatement(queryGen)) {

					/*Viene impostato il parametro della query con il genere specificato dall'utente utilizzando 
					stmt.setString(1, gen). Questo significa che il valore inserito dall'utente sarà sostituito 
					nel posto del segnaposto ? nella query SQL. */

					stmt.setString(1, gen); 

					/*La query viene eseguita tramite stmt.executeQuery(), e i risultati vengono memorizzati in un 
					oggetto ResultSet. Il programma itera attraverso il ResultSet tramite un ciclo while (rs.next())
					per recuperare ogni riga dei risultati. */

					ResultSet rs = stmt.executeQuery();

					/* Il metodo next() viene utilizzato per spostare il cursore del ResultSet alla prossima riga di dati.
					 Inizialmente, il cursore si trova prima della prima riga di risultati. Controllo della presenza di dati: 
					 Il metodo next() restituisce true se il cursore viene spostato con successo alla prossima riga di dati 
					 nel ResultSet. Restituisce false se non ci sono ulteriori righe di dati disponibili.
					 Comunemente, viene utilizzato all'interno di un ciclo while per iterare attraverso tutte le righe di 
					 dati restituite dalla query. Ad ogni iterazione del ciclo, il metodo next() sposta il cursore alla riga 
					 successiva e restituisce true se ci sono ancora righe disponibili, altrimenti restituisce false, 
					 interrompendo il ciclo. */

					/*Estrae i valori di ogni colonna per ogni riga del ResultSet utilizzando i metodi 
					 come rs.getInt() e rs.getString(). Poi, stampa le informazioni del film a console. */

					while (rs.next()) {
						int id = rs.getInt("id");
						String titolo = rs.getString("titolo");
						String regista = rs.getString("regista");
						String genere = rs.getString("genere");
						int anno = rs.getInt("anno");
						System.out.print(id + " ");
						System.out.print(titolo + " ");
						System.out.print(regista + " ");
						System.out.print(genere + " ");
						System.out.print(anno + "\n");

					}
				} catch (SQLException e) {
					// Gestione dell'eccezione per la connessione al database o l'esecuzione della
					// query
					System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
					e.printStackTrace();
				}

			}  else if (scelta == 3) {
	
					System.out.println("inserisci l'anno del film da ricercare");
					Scanner input3 = new Scanner(System.in);
					String ann = input3.nextLine();
	
					String queryAnn = "SELECT * FROM film WHERE anno = (?)";
	
	
					try {
						Connection conn = DriverManager.getConnection(url + dbName, user, password);
						PreparedStatement stmt = conn.prepareStatement(queryAnn);
	
						stmt.setString(1, ann); 
	
						ResultSet rs = stmt.executeQuery();
	
						while (rs.next()) {
							int id = rs.getInt("id");
							String titolo = rs.getString("titolo");
							String regista = rs.getString("regista");
							String genere = rs.getString("genere");
							int anno = rs.getInt("anno");
							System.out.print(id + " ");
							System.out.print(titolo + " ");
							System.out.print(regista + " ");
							System.out.print(genere + " ");
							System.out.print(anno + "\n");
	
						}
					} catch (SQLException e) {
						// Gestione dell'eccezione per la connessione al database o l'esecuzione della
						// query
						System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
						e.printStackTrace();
					}
					
				
				} else if (scelta == 4) {
	
					String insertQuery= "SELECT * FROM film";
	
	
					try 
					{ 	Connection conn = DriverManager.getConnection(url + dbName, user, password);
	
						Statement stmt = conn.createStatement();
			             ResultSet rs = stmt.executeQuery(insertQuery);
	
						while (rs.next()) {
							int id = rs.getInt("id");
							String titolo = rs.getString("titolo");
							String regista = rs.getString("regista");
							String genere = rs.getString("genere");
							int anno = rs.getInt("anno");
							System.out.print(id + " ");
							System.out.print(titolo + " ");
							System.out.print(regista + " ");
							System.out.print(genere + " ");
							System.out.print(anno + "\n");
	
						}
						
						
					} catch (SQLException e) {
						// Gestione dell'eccezione per la connessione al database o l'esecuzione della
						// query
						System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
						e.printStackTrace();
					}
					
					System.out.println("Se vuoi modificare l'anno di un film o cancellarlo inserisci prima il suo id: ");
					Scanner scan2 = new Scanner(System.in);
					int id1 = scan2.nextInt();
					scan2.nextLine();
					
					System.out.println("Vuoi aggiornare l'anno o cancellare un film? Premi 8 oppure 9");
					int scelta2 = scan2.nextInt();
					
					if (scelta2 == 8) {
						System.out.println("Ora inserisci l'anno giusto");
						int ann = scan2.nextInt();
						
						String query3 = "UPDATE film SET anno = (?) WHERE id = (?)";
						try {
						Connection conn = DriverManager.getConnection(url + dbName, user, password);
						PreparedStatement stmt = conn.prepareStatement(query3);
						stmt.setInt(1, ann); 
						stmt.setInt(2, id1);
	
						int rowAffected = stmt.executeUpdate();
						
	
						
						} catch (SQLException e) {
							// Gestione dell'eccezione per la connessione al database o l'esecuzione della
							// query
							System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
							e.printStackTrace();
						}
						
					} else if (scelta2 == 9) {
						
						String query3 = "DELETE FROM film WHERE id = (?)";
						
						try {
						Connection conn = DriverManager.getConnection(url + dbName, user, password);
						PreparedStatement stmt = conn.prepareStatement(query3);
						stmt.setInt(1, id1); 
	
						int rowsAffected = stmt.executeUpdate();
						
	
						
						} catch (SQLException e) {
							// Gestione dell'eccezione per la connessione al database o l'esecuzione della
							// query
							System.out.println("Errore durante l'inserimento dei dati nella tabella 'film':");
							e.printStackTrace();
						}
						
					}
					
					
				} else if (scelta == 0) {
					System.out.println("Grazie per aver interagito con noi! A presto");
				}
			
		} while (scelta != 0);
		
	}
}
