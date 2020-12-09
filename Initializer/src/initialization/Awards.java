package initialization;

public class Awards {
	  private int year;
	  private String category;
	  private boolean winner;
	  private String entity;

	  // constructor for Awards
	  Awards(int year, String category, boolean winner, String entity) {
	    this.year = year;
	    this.category = category;
	    this.winner = winner;
	    this.entity = entity;

	  }

	  // getter for year
	  public int getYear() {
	    return year;

	  }

	  // getter for category
	  public String getCategory() {
	    return category;

	  }

	  // getter for winner
	  public boolean getWinner() {
	    return winner;

	  }

	  // getter for entity
	  public String getEntity() {
	    return entity;

	  }

	  // setting for year
	  public void setYear(int newYear) {
	    this.year = newYear;

	  }

	  // setter for category
	  public void setCategory(String newCategory) {
	    this.category = newCategory;

	  }

	  // setter for winner
	  public void setWinner(boolean newWinner) {
	    this.winner = newWinner;

	  }

	  // setter for entity
	  public void setEntity(String newEntity) {
	    this.entity = newEntity;

	  }
	}
