class Person {

	String firstName
	String lastName
	List<Address> addresses
	Date created

	static hasMany = [addresses:Address]

    static constraints = {
        firstName(nullable:false)
        lastName(nullable:false)
        created(nullable:false)
    } 	
        
}
