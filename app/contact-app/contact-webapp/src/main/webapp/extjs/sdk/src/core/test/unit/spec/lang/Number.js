describe("Ext.Number", function(){
    var Number = Ext.Number;
    
    describe("constraining a number", function(){
        describe("integers", function(){
            describe("if the number is within the constaints", function(){
                it("should leave the number alone if it is equal to the min and the max", function(){
                    expect(Number.constrain(1, 1, 1)).toEqual(1);
                });
                
                it("should leave the number alone if it is equal to the min", function(){
                    expect(Number.constrain(1, 1, 5)).toEqual(1);
                });
                
                it("should leave the number alone if it is equal to the max", function(){
                    expect(Number.constrain(5, 1, 5)).toEqual(5);
                });
                
                it("should leave the number alone if it is within the min and the max", function(){
                    expect(Number.constrain(3, 1, 5)).toEqual(3);
                });
                
                it("should leave a negative number alone if it is within the min and the max", function(){
                    expect(Number.constrain(-3, -5, -1)).toEqual(-3);
                });
            });
            
            describe("if the number is not within the constraints", function(){
                it("should make the number equal to the min value", function(){
                    expect(Number.constrain(1, 3, 5)).toEqual(3);
                });
                
                it("should make the number equal to the max value", function(){
                    expect(Number.constrain(100, 1, 5)).toEqual(5);
                });
                
                describe("and the number is negative", function(){
                    it("should make the number equal to the min value", function(){
                        expect(Number.constrain(-10, -50, -30)).toEqual(-30);
                    });
                    
                    it("should make the number equal to the max value", function(){
                        expect(Number.constrain(-100, -50, -30)).toEqual(-50);
                    });
                });
            });

            describe("constrain NaN", function() {
                it("should never constrain a NaN between two numbers", function(){
                    expect(isNaN(Number.constrain(NaN, 3, 5))).toEqual(true);
                });                
                it("should never constrain a NaN between zero and undefined", function(){
                    expect(isNaN(Number.constrain(NaN, 0, undefined))).toEqual(true);
                });                
                it("should never constrain a NaN between undefined and zero", function(){
                    expect(isNaN(Number.constrain(NaN, undefined, 0))).toEqual(true);
                });                
                it("should never constrain a NaN between a number and undefined", function(){
                    expect(isNaN(Number.constrain(NaN, 10, undefined))).toEqual(true);
                });                
                it("should never constrain a NaN between undefined and a number", function(){
                    expect(isNaN(Number.constrain(NaN, undefined, 10))).toEqual(true);
                });                
                it("should never constrain a NaN between two undefined values", function(){
                    expect(isNaN(Number.constrain(NaN, undefined, undefined))).toEqual(true);
                });                
            });

            describe("constrain with NaN/undefined max", function() {
                it("should ignore NaN max", function(){
                    expect(Number.constrain(2, 1, NaN)).toEqual(2);
                });                
                it("should ignore NaN max and limit to min", function(){
                    expect(Number.constrain(2, 5, NaN)).toEqual(5);
                });                
                it("should ignore undefined max", function(){
                    expect(Number.constrain(2, 1, undefined)).toEqual(2);
                });                
                it("should ignore undefined max and limit to min", function(){
                    expect(Number.constrain(2, 5, undefined)).toEqual(5);
                });                
            });

            describe("constrain with NaN/undefined min", function() {
                it("should ignore NaN min", function(){
                    expect(Number.constrain(2, NaN, 5)).toEqual(2);
                });                
                it("should ignore NaN min and limit to max", function(){
                    expect(Number.constrain(20, NaN, 5)).toEqual(5);
                });                
                it("should ignore undefined min", function(){
                    expect(Number.constrain(2, undefined, 5)).toEqual(2);
                });                
                it("should ignore undefined min and limit to max", function(){
                    expect(Number.constrain(20, undefined, 5)).toEqual(5);
                });                
            });

            describe("constrain with NaN/undefined min/max", function() {
                it("should ignore NaN min/max", function(){
                    expect(Number.constrain(2, NaN, NaN)).toEqual(2);
                });                
                it("should ignore undefined min/max", function(){
                    expect(Number.constrain(2, undefined, undefined)).toEqual(2);
                });                
                it("should ignore NaN min and undefined max", function(){
                    expect(Number.constrain(2, NaN, undefined)).toEqual(2);
                });                
                it("should ignore undefined min and NaN max", function(){
                    expect(Number.constrain(2, undefined, NaN)).toEqual(2);
                });                
            });
        });
        
        describe("floating point numbers", function(){
            describe("if the number is within the constaints", function(){
                it("should leave the number alone", function(){
                    expect(Number.constrain(3.3, 3.1, 3.5)).toEqual(3.3);
                });
                
                it("should leave a negative number alone", function(){
                    expect(Number.constrain(-3.3, -3.5, -3.1)).toEqual(-3.3);
                });
            });
            
            describe("and the number is negative", function(){
                it("should make the number equal to the min value", function(){
                    expect(Number.constrain(-3.3, -3.1, -3)).toEqual(-3.1);
                });
                
                it("should make the number equal to the max value", function(){
                    expect(Number.constrain(-2.1, -3.1, -3)).toEqual(-3);
                });
            });
        });
    });
    
    describe("toFixed", function(){
        
        var f = Number.toFixed;
        
        it("should return a string", function(){
            expect(typeof f(1)).toEqual('string');
        });
        
        it("should default precision to 0", function(){
            expect(f(1.23456)).toEqual('1');
        });
        
        it("should output the correct number of decimal places", function(){
            expect(f(1, 3)).toEqual('1.000');
        });
        
        it("should round correctly", function(){
            expect(f(1.9834657, 1)).toEqual('2.0');
        });
        
        it("should round with negative numbers", function(){
            expect(f(-3.4265, 2)).toEqual('-3.43');
        });
    });

    describe("snap", function(){

        // Params are (value, snapincrement, minValue, maxValue)
        var snap = Number.snap;

        it("should enforce minValue if increment is zero", function(){
            expect(snap(40, 0, 50, 100)).toEqual(50);
        });

        it("should enforce maxValue if increment is zero", function(){
            expect(snap(5000, 0, 0, 100)).toEqual(100);
        });

        it("should enforce minValue if passed", function(){
            expect(snap(0, 2, 1, 100)).toEqual(1);
        });

        it("should not enforce a minimum if no minValue passed", function(){
            expect(snap(21, 2, undefined, 100)).toEqual(22);
        });

        it("should enforce maxValue if passed", function(){
            expect(snap(1000, 2, undefined, 100)).toEqual(100);
        });

        it("should not enforce a maximum if no maxValue passed", function(){
            expect(snap(21, 2, undefined, undefined)).toEqual(22);
        });

        it("should snap to a snap point based upon zero", function(){
            expect(snap(56, 2, 55, 65)).toEqual(56);
            expect(snap(100, 2, 55, 66)).toEqual(66);
        });

        it("should enforce the minValue", function(){
            expect(snap(20, 2, 55, 65)).toEqual(55);
        });

        it("should round to the nearest snap point", function(){
            expect(snap(4, 5, 0, 100)).toEqual(5);
        });
        
        it("should snap negative numbers", function() {
           expect(snap(-9, 10, -100, 0)).toBe(-10);
           expect(snap(-1, 10, -100, 0)).toBe(0);
        });

    });

    describe("snapInRange", function(){

        // Params are (value, snapincrement, minValue, maxValue)
        var snapInRange = Number.snapInRange;

        it("should enforce minValue if increment is zero", function(){
            expect(snapInRange(50, 0, 0, 100)).toEqual(50);
        });

        it("should enforce maxValue if increment is zero", function(){
            expect(snapInRange(5000, 0, 0, 100)).toEqual(100);
        });

        it("should enforce minValue if passed", function(){
            expect(snapInRange(0, 2, 1, 100)).toEqual(1);
        });

        it("should not enforce a minimum if no minValue passed", function(){
            expect(snapInRange(21, 2, undefined, 100)).toEqual(22);
        });

        it("should enforce maxValue if passed", function(){
            expect(snapInRange(1000, 2, undefined, 100)).toEqual(100);
        });

        it("should not enforce a maximum if no maxValue passed", function(){
            expect(snapInRange(21, 2, undefined, undefined)).toEqual(22);
        });

        // Valid values are 55, 57, 59, 61, 63, 65
        it("should snap to a snap point based upon the minValue", function(){
            expect(snapInRange(56, 2, 55, 65)).toEqual(57);
        });

        it("should enforce the minValue", function(){
            expect(snapInRange(20, 2, 55, 65)).toEqual(55);
        });

        // Valid values are still 55, 57, 59, 61, 63, 65
        it("should snap to a snap point based upon the minValue even if maxValue is not on a snap point", function(){
            expect(snapInRange(100, 2, 55, 66)).toEqual(67);
        });

        it("should round to the nearest snap point", function() {
            expect(snapInRange(4, 5, 0, 100)).toEqual(5);

            expect(snapInRange(10, 10, 1, 101)).toBe(11);
            expect(snapInRange(11, 10, 1, 101)).toBe(11);
            expect(snapInRange(12, 10, 1, 101)).toBe(11);
            expect(snapInRange(20, 10, 1, 101)).toBe(21);
            expect(snapInRange(21, 10, 1, 101)).toBe(21);
            expect(snapInRange(22, 10, 1, 101)).toBe(21);
        });
        
        it("should handle negative ranges", function() {
            expect(snapInRange(-10, 10, -101, -1)).toBe(-11);
            expect(snapInRange(-11, 10, -101, -1)).toBe(-11);
            expect(snapInRange(-12, 10, -101, -1)).toBe(-11);
            expect(snapInRange(-20, 10, -101, -1)).toBe(-21);
            expect(snapInRange(-21, 10, -101, -1)).toBe(-21);
            expect(snapInRange(-22, 10, -101, -1)).toBe(-21);
        });
    });
    
    describe("from", function() {
        var from = Ext.Number.from;
       
        it("should handle numbers", function() {
            expect(from(2, 1)).toBe(2);
            expect(from(-2, 1)).toBe(-2);
            expect(from(999999, 1)).toBe(999999);
            expect(from(-999999, 1)).toBe(-999999);
            expect(from(999999.999, 1)).toBe(999999.999);
            expect(from(-999999.999, 1)).toBe(-999999.999);
        });
           
        it("should handle strings that represent numbers", function() {
            expect(from("2", 1)).toBe(2);
            expect(from("-2", 1)).toBe(-2);
            expect(from("999999", 1)).toBe(999999);
            expect(from("-999999", 1)).toBe(-999999);
            expect(from("999999.999", 1)).toBe(999999.999);
            expect(from("-999999.999", 1)).toBe(-999999.999);
        });
           
        it("should handle infinity", function() {
            expect(from(1/0, 1)).toBe(window.Number.POSITIVE_INFINITY);
            expect(from(-1/0, 1)).toBe(window.Number.NEGATIVE_INFINITY);
        });
           
        it("should return default value if value is not a number or numeric string", function() {
            expect(from("", 100)).toBe(100); 
            expect(from(true, 100)).toBe(100); 
            expect(from(false, 100)).toBe(100); 
            expect(from("I would like to be a number", 100)).toBe(100); 
            expect(from("12345ImAlmostANumber", 100)).toBe(100); 
        });
    });
    
    describe("randomInt", function() {
        var randomInt = Ext.Number.randomInt;
        it("should return a random integer within the specified range", function() {
            expect(randomInt(0, 100)).not.toBeLessThan(0);
            expect(randomInt(0, 100)).not.toBeGreaterThan(100);
            expect(randomInt(-100, 0)).not.toBeLessThan(-100);
            expect(randomInt(-100, 0)).not.toBeGreaterThan(0);
            expect(randomInt(1, 1)).toBe(1);
            expect(randomInt(1, 1)).toBe(1);
        });
    });

});
