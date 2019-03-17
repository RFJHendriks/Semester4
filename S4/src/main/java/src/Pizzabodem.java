package src;

public class Pizzabodem {
	private String naam;
	private Double diameter;
	private String omschrijving;
	private Double toeslag;
	private Boolean beschikbaar;
	
	public Pizzabodem(String naam, Double diameter, String omschrijving, Double toeslag, Boolean beschikbaar) {
		this.naam = naam;
		this.diameter = diameter;
		this.omschrijving = omschrijving;
		this.toeslag = toeslag;
		this.beschikbaar = beschikbaar;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public Double getDiameter() {
		return diameter;
	}

	public void setDiameter(Double diameter) {
		this.diameter = diameter;
	}

	public String getOmschrijving() {
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public Double getToeslag() {
		return toeslag;
	}

	public void setToeslag(Double toeslag) {
		this.toeslag = toeslag;
	}

	public Boolean getBeschikbaar() {
		return beschikbaar;
	}

	public void setBeschikbaar(Boolean beschikbaar) {
		this.beschikbaar = beschikbaar;
	}
	
	public String toString() {
		return "Naam: " + naam + " Diameter: " + diameter + " Omschrijving: " + omschrijving + " Toeslag: " + toeslag + "Beschikbaar: " + beschikbaar;
	}
	
	
	
	
	

}
