class Address {

    String address
    String city
    String state
    String zipPostal
    String country
    Date created
    
    static belongsTo = Person

    static constraints = {
        address(nullable:false)
        city(nullable:false)
        state(nullable:false)
        zipPostal(nullable:false)
        country(nullable:false)
        created(nullable:false)
    }

}
