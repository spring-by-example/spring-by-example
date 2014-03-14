Based on Spring Modules Validation project version 0.9.  Valang is being enhanced 
to have more features and better performance.  The project is still under 
the Apache License, Version 2.0 and at some point may be contributed back into the 
Spring Modules project once enhancements are complete.
        

Release Notes
--------------
0.97 - Upgraded to Spring 4.0.2.

0.96 -  Upgraded to Spring 3.2.

0.95 -  Upgraded to Spring 3.1.

0.94 -  Upgraded to Spring 3.0.

0.93 -  Made specific comparison classes for each operator for a performance improvement.

		Changed IS WORD and IS BLANK to use Commons Lang StringUtils, which will change the 
		behavior slightly but should be more accurate to the description of the validation.

		Change Operator from interfaces to an enum and removed OperatorConstants.
		
		Fixed bytecode generation to handle Maps, Lists, and Arrays.

0.92 -  Removed custom dependency injection since functions can be configured in Spring.

		Added auto-discovery of FunctionWrapper beans from the Spring context to 
		go with existing auto-discovery of FunctionDefinition beans. 

0.91 -  Bytecode generation added to DefaultVisitor as a replacement for reflection accessing 
		simple properties (BeanPropertyFunction) for a significant performance improvement.

		Basic enum comparison support.  For example, in the expression 
		"personType EQUALS ['STUDENT']" the personType is an enum and the value 'STUDENT' will 
		be convereted to an enum for comparison.  The value must match an enum value on the 
		type being compared or an exception will be thrown.  For better performance the 
		full class name can be specified so the enum can be retrieved during parsing.
		For example "personType EQUALS ['org.springmodules.validation.example.PersonType.STUDENT']" 
		or for an inner enum class 
		"personType EQUALS ['org.springmodules.validation.example.Person$PersonType.STUDENT']".
		
		Where clause support For example, in the expression 
		"price < 100 WHERE personType EQUALS ['STUDENT']" the part of the expression 
		"price < 100" will only be evaluated if the personType is 'STUDENT'.  Otherwise 
		the validation will be skipped. 
		
		Improved performance of 'IN'/'NOT IN' if comparing a value to a java.util.Set 
		it will use Set.contains(value).  Static lists of Strings (ex: 'A', 'B', 'C') 
		are now stored in a Set instead of an ArrayList.

		Functions can be configured in Spring, but need to have their scope set as prototype 
		and use a FunctionWrapper that is also a prototype bean with <aop:scoped-proxy> 
		set on it.

		Removed servlet dependency from Valang project except for the custom JSP tag 
		ValangValidateTag needing it, but running Valang no longer requires it.
		This involved removing ServletContextAware from it's custom dependency injection.
		If someone was using this in a custom function, the function can now be configured 
		directly in Spring and Spring can inject any "aware" values.

		Changed logging to use SLF4J api.

		
To Do
---------
ability to turn off auto-registration of custom functions
bytecode genertaton for nested properties
better e-mail validation using commons validator
possibly add DEPENDS clause to not have a rule run if another doesn't pass - http://jira.springframework.org/browse/MOD-152
validate a list of objects (like addresses) - http://jira.springframework.org/browse/MOD-64
