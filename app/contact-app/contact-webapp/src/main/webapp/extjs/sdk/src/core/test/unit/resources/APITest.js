function APITest(expected, actual, message){
    it('should implement the API: ' + (message||''), function(){
        APITest.testKind(expected, actual, message);
    });
    
    if (!(typeof expected == 'object' || typeof expected == 'function')) {
        return;
    }
    
    for (var property in expected) {
        APITest(
            expected[property],
            actual[property],
            (message ? [message] : []).concat([property]).join('.')
        );
    }
    
    if (typeof expected.prototype == 'object') for (var property in expected.prototype) {
        APITest(
            expected.prototype[property],
            actual.prototype[property],
            (message ? [message] : []).concat(['prototype',property]).join('.')
        );
    }
}

APITest.testKind = function(expected, actual, message){
    expect(typeof actual).toBe(typeof expected);
    
    if (typeof expected == 'boolean') expect(actual).toBe(expected);
    if (expected instanceof Array) expect(actual instanceof Array).toBe(true);
    if (expected instanceof String) expect(actual instanceof String).toBe(true);
    if (expected instanceof Number) expect(actual instanceof Number).toBe(true);
    if (expected instanceof Date) expect(actual instanceof Date).toBe(true);
    if (expected instanceof RegExp) expect(actual instanceof RegExp).toBe(true);
    if (expected instanceof Function) expect(actual instanceof Function).toBe(true);
};
