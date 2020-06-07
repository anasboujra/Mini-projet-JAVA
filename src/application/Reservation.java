package application;

import java.time.LocalDate;

public class Reservation {

	private String codeReservation;
	private LocalDate dateReservation;
	private LocalDate dateDepart;
	private LocalDate dateRetour;
	private String status;
	public Reservation() {
		super();
	}
	 
	public Reservation(String codeReservation, LocalDate dateReservation, LocalDate dateDepart, LocalDate dateRetour,
			String status) {
		super();
		this.codeReservation = codeReservation;
		this.dateReservation = dateReservation;
		this.dateDepart = dateDepart;
		this.dateRetour = dateRetour;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCodeReservation() {
		return codeReservation;
	}
	public void setCodeReservation(String codeReservation) {
		this.codeReservation = codeReservation;
	}
	public LocalDate getDateReservation() {
		return dateReservation;
	}
	public void setDateReservation(LocalDate dateReservation) {
		this.dateReservation = dateReservation;
	}
	public LocalDate getDateDepart() {
		return dateDepart;
	}
	public void setDateDepart(LocalDate dateDepart) {
		this.dateDepart = dateDepart;
	}
	public LocalDate getDateRetour() {
		return dateRetour;
	}
	public void setDateRetour(LocalDate dateRetour) {
		this.dateRetour = dateRetour;
	}
	
	 
}
