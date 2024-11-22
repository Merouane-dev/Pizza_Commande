package directpizzam1;

public class pizza {
	
	String nomPizza ;
	double  prixPizza;
	Integer numeroPizza;
	
	
	public pizza(String nomPizza, double prixPizza, Integer numeroPizza) {
		 
		this.nomPizza = nomPizza;
		this.prixPizza = prixPizza;
		this.numeroPizza = numeroPizza;
	}


	@Override
	public String toString() {
		return "Pizza wilfrid [nomPizza=" + nomPizza + ", prixPizza=" + prixPizza + ", numeroPizza=" + numeroPizza + "]";
	}
	
	

}
