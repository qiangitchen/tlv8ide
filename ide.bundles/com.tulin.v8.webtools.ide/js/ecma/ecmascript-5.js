/*
 * A template file of ECMA Script 5 
 * 
 * @author shinsuke
 */

// Value Properties of the Global Object
/**
 * A value representing Not-A-Number.
 * 
 */
var NaN = {};
/**
 * A numeric value representing infinity.
 * 
 */
var Infinity = {};
/**
 * The value undefined.
 * 
 */
var undefined = {};

// Function Properties of the Global Object
/**
 * Evaluates a string of JavaScript code without reference to a particular object.
 * 
 * @param {String} str A string representing a JavaScript expression, statement, or sequence of statements. The expression can include variables and properties of existing objects.
 * @returns {Object} 
 */
function eval(str){}
/**
 * Parses a string argument and returns an integer of the specified radix or base.
 * 
 * @param {String} str A string that represents the value you want to parse. 
 * @param {Number} radix An integer that represents the radix of the above mentioned string. 
 * @returns {Number} 
 */
function parseInt(str, radix){}
/**
 * Parses a string argument and returns a floating point number. 
 * 
 * @param {String} str A string that represents the value you want to parse. 
 * @returns {Number} 
 */
function parseFloat(str){}
/**
 * Evaluates an argument to determine if it is not a number.
 * 
 * @param {Object} testValue The value you want to evaluate.
 * @returns {Boolean} 
 */
function isNaN(testValue){}
/**
 * Evaluates an argument to determine whether it is a finite number. 
 * 
 * @param {Number} number The number to evaluate. 
 * @returns {Boolean} 
 */
function isFinite(number){}
/**
 * Decodes a Uniform Resource Identifier (URI) previously created by encodeURI or by a similar routine. 
 * 
 * @param {String} encodedURI A complete, encoded Uniform Resource Identifier. 
 * @returns {String} 
 */
function decodeURI(encodedURI){}
/**
 * Decodes a Uniform Resource Identifier (URI) component previously created by encodeURIComponent or by a similar routine. 
 * 
 * @param {String} encodedURI A complete, encoded Uniform Resource Identifier. 
 * @returns {String} 
 */
function decodeURIComponent(encodedURI){}
/**
 * Encodes a Uniform Resource Identifier (URI) by replacing each instance of certain characters by one, two, three, or four escape sequences representing the UTF-8 encoding of the character (will only be four escape sequences for characters composed of two "surrogate" characters).
 * 
 * @param {String} URI A complete Uniform Resource Identifier.
 * @returns {String} 
 */
function encodeURI(URI){}
/**
 * Encodes a Uniform Resource Identifier (URI) component by replacing each instance of certain characters by one, two, three, or four escape sequences representing the UTF-8 encoding of the character (will only be four escape sequences for characters composed of two "surrogate" characters).
 * 
 * @param {String} str A component of a URI.
 * @returns {String} 
 */
function encodeURIComponent(str){}


// Class
/**
 * Object class.
 * Creates an object wrapper.
 * 
 * @class Object
 * @returns {Object}  
 */
function Object() {
	// properties
	/**
	 * Returns a reference to the Object function that created the instance's prototype. Note that the value of this property is a reference to the function itself, not a string containing the function's name, but it isn't read only (except for primitive Boolean, Number or String values: 1, true, "read-only").
	 * 
	 * @property {Object} constructor Specifies the function that creates an object's prototype.
	 */
	this.constructor = {}
	// methods
	/**
	 * Returns a boolean indicating whether the object has the specified property.
	 * 
	 * @param {Object} prop The name of the property to test.
	 * @returns {Boolean} 
	 */
	Object.prototype.hasOwnProperty = function(prop){}
	/**
	 * Tests for an object in another object's prototype chain.
	 * 
	 * @param {Object} obj the object whose prototype chain will be searched
	 * @returns {Boolean} 
	 */
	Object.prototype.isPrototypeOf = function(obj){}
	/**
	 * Returns a boolean indicating whether the specified property is enumerable.
	 * 
	 * @param {Object} prop The name of the property to test.
	 */
	Object.prototype.propertyIsEnumerable = function(prop){}
	/**
	 * Returns a string representing the object. This method is meant to be overriden by derived objects for locale-specific purposes.
	 * 
	 * @returns {String} 
	 */
	Object.prototype.toLocaleString = function(){}
	/**
	 * Returns a string representing the object. 
	 * 
	 * @returns {String} 
	 */
	Object.prototype.toString = function(){}
	/**
	 * Returns the primitive value of the specified object 
	 * 
	 * @returns {Object} 
	 */
	Object.prototype.valueOf = function(){}
}
// Object class: static properties and methods

// Object class: prototype

/**
 * Creates a new object with the specified prototype object and properties.
 * 
 * @param {Object} proto The object which should be the prototype of the newly-created object.
 * @param {Object} propertiesObject If specified and not undefined, an object whose enumerable own properties (that is, those properties defined upon itself and not enumerable properties along its prototype chain) specify property descriptors to be added to the newly-created object, with the corresponding property names.
 * @returns {Object} returnDescription
 * @static
 */
Object.create = function(proto, propertiesObject){}
/**
 * Defines a new property directly on an object, or modifies an existing property on an object, and returns the object.
 * 
 * @param {Object} obj The object on which to define the property.
 * @param {Object} prop The name of the property to be defined or modified.
 * @param {descriptor} The descriptor for the property being defined or modified. 
 * @static
 */
Object.defineProperty = function(obj, prop, descriptor){}
/**
 * Defines new or modifies existing properties directly on an object, returning the object.
 * 
 * @param {Object} obj The object on which to define or modify properties. 
 * @param {Object} props An object whose own enumerable properties constitute descriptors for the properties to be defined or modified.
 * @static
 */
Object.defineProperties = function(obj, props){}
/**
 * Returns a property descriptor for an own property (that is, one directly present on an object, not present by dint of being along an object's prototype chain) of a given object.
 * 
 * @param {Object} obj The object in which to look for the property.
 * @param {Object} prop The name of the property whose description is to be retrieved
 * @returns {Object} 
 * @static
 */
Object.getOwnPropertyDescriptor = function(obj, prop){}
/**
 * Returns an array of all own enumerable properties found upon a given object, in the same order as that provided by a for-in loop (the difference being that a for-in loop enumerates properties in the prototype chain as well).
 * 
 * @param {Object} obj The object whose enumerable own properties are to be returned.
 * @returns {Object} 
 * @static
 */
Object.keys = function(obj){}
/**
 * Returns an array of all properties (enumerable or not) found upon a given object.
 * 
 * @param {Object} obj The object whose enumerable own properties are to be returned.
 * @returns {Array} 
 * @static
 */
Object.getOwnPropertyNames = function(obj){}
/**
 * Returns the prototype of the specified object.
 * 
 * @param {Object} obj The object whose prototype is to be returned.
 * @returns {Object} 
 * @static
 */
Object.getPrototypeOf = function(obj){}
/**
 * Prevents new properties from ever being added to an object (i.e. prevents future extensions to the object).
 * 
 * @param {Object} obj The object which should be made non-extensible.
 * @static
 */
Object.preventExtensions = function(obj){}
/**
 * Determines if an object is extensible (whether it can have new properties added to it).
 * 
 * @param {Object} obj The object which should be checked.
 * @returns {Boolean} 
 * @static
 */
Object.isExtensible = function(obj){}
/**
 * Seals an object, preventing new properties from being added to it and marking all existing properties as non-configurable. Values of present properties can still be changed as long as they are writable.
 * 
 * @param {Object} obj The object which should be sealed.
 * @static
 */
Object.seal = function(obj){}
/**
 * Determine if an object is sealed.
 * 
 * @param {Object} obj The object which should be checked.
 * @returns {Boolean} 
 * @static
 */
Object.isSealed = function(obj){}
/**
 * Freezes an object: that is, prevents new properties from being added to it; prevents existing properties from being removed; and prevents existing properties, or their enumerability, configurability, or writability, from being changed.  In essence the object is made effectively immutable.  The method returns the object being frozen.
 * 
 * @param {Object} obj The object which should be frozen.
 * @static
 */
Object.freeze = function(obj){}
/**
 * Determine if an object is frozen.
 * 
 * @param {Object} obj The object which should be checked.
 * @returns {Boolean} 
 * @static
 */
Object.isFrozen = function(obj){}

/**
 * Function class.
 * Every function in JavaScript is actually a Function object.
 * 
 * @class Function
 * @param {String} argN Names to be used by the function as formal argument names. Each must be a string that corresponds to a valid JavaScript identifier or a list of such strings separated with a comma; for example "x", "theValue", or "a,b".
 * @param {String} functionBody     A string containing the JavaScript statements comprising the function definition.
 * @returns {Function} 
 */
function Function(argN, functionBody) {
	// properties
	/**
	 * Specifies the number of arguments expected by the function. 
	 * 
	 * @property {Number} length Specifies the number of arguments expected by the function. 
	 */
	this.length = 1;
	
	// methods
	/**
	 * Calls a function with a given this value and arguments provided as an array.
	 * 
	 * @param {Object} thisArg Determines the value of this inside fun. If thisArg is null or undefined, this will be the global object. Otherwise, this will be equal to Object(thisArg) (which is thisArg if thisArg is already an object, or a String, Boolean, or Number if thisArg is a primitive value of the corresponding type). Therefore, it is always true that typeof this == "object" when the function executes.
	 * @param {Array} argsArray An argument array for the object, specifying the arguments with which fun should be called, or null or undefined if no arguments should be provided to the function. 
	 * @returns {Object} 
	 */
	Function.prototype.apply = function(thisArg, argsArray){}
	/**
	 * Calls a function with a given this value and arguments provided individually.
	 * 
	 * @param {Object} thisArg Determines the value of this inside fun. If thisArg is null or undefined, this will be the global object. Otherwise, this will be equal to Object(thisArg) (which is thisArg if thisArg is already an object, or a String, Boolean, or Number if thisArg is a primitive value of the corresponding type). Therefore, it is always true that typeof this == "object" when the function executes.
	 */
	Function.prototype.call = function(thisArg, argN){}
}
// Function class: static properties and methods
/**
 * Specifies the number of arguments expected by the function. 
 * 
 * @property {Number} length Specifies the number of arguments expected by the function. 
 * @static
 */
Function.length = 1;

// Function class: prototype

/**
 * Array class.
 * Array is a global object that may be used to construct Array instances.
 * 
 * @class Array
 * @param {Array} len The initial length of the array. You can access this value using the length property. If the value specified is not a number, an array of length 1 is created, with the first element having the specified value. The maximum length allowed for an array is 4,294,967,295.
 * @returns {Array} 
 */
function Array(len) {
	// properties
	
	// methods
	/**
	 * Returns a new array comprised of this array joined with other array(s) and/or value(s).
	 * 
	 * @param {Number} valueN Arrays and/or values to concatenate to the resulting array.
	 * @returns {Array} 
	 */
	Array.prototype.concat = function(valueN){}
	/**
	 * Joins all elements of an array into a string.
	 * 
	 * @param {String} separator Specifies a string to separate each element of the array. The separator is converted to a string if necessary. If omitted, the array elements are separated with a comma. 
	 * @returns {String} 
	 */
	Array.prototype.join = function(separator){}
	/**
	 * Removes the last element from an array and returns that element.
	 * 
	 * @returns {Object} 
	 */
	Array.prototype.pop = function(){}
	/**
	 * Mutates an array by appending the given elements and returning the new length of the array.
	 * 
	 * @param {Object} elementN The elements to add to the end of the array. 
	 * @returns {Number} 
	 */
	Array.prototype.push = function(elementN){}
	/**
	 * Reverses an array in place.  The first array element becomes the last and the last becomes the first.
	 * 
	 */
	Array.prototype.reverse = function(){}
	/**
	 * Removes the first element from an array and returns that element. This method changes the length of the array.
	 * 
	 * @returns {Object} 
	 */
	Array.prototype.shift = function(){}
	/**
	 * Returns a one-level deep copy of a portion of an array.
	 * 
	 * @param {Number} begin Zero-based index at which to begin extraction. As a negative index, start indicates an offset from the end of the sequence. slice(-2) extracts the second-to-last element and the last element in the sequence.
	 * @param {Number} end Zero-based index at which to end extraction. slice extracts up to but not including end. slice(1,4) extracts the second element through the fourth element (elements indexed 1, 2, and 3).  As a negative index, end indicates an offset from the end of the sequence. slice(2,-1) extracts the third element through the second-to-last element in the sequence. If end is omitted, slice extracts to the end of the sequence. 
	 * @returns {Array} 
	 */
	Array.prototype.slice = function(begin, end){}
	/**
	 * Sorts the elements of an array in place.
	 * 
	 * @param {Function} compareFunction Specifies a function that defines the sort order. If omitted, the array is sorted lexicographically (in dictionary order) according to the string conversion of each element.
	 */
	Array.prototype.sort = function(compareFunction){}
	/**
	 * Changes the content of an array, adding new elements while removing old elements.
	 * 
	 * @param {Number} index Index at which to start changing the array. If negative, will begin that many elements from the end.
	 * @param {Number} howMany An integer indicating the number of old array elements to remove. If howMany is 0, no elements are removed. In this case, you should specify at least one new element. If no howMany parameter is specified (second syntax above, which is a SpiderMonkey extension), all elements after index are removed. 
	 * @param {Object} elementN The elements to add to the array. If you don't specify any elements, splice simply removes elements from the array. 
	 * @returns {Array} 
	 */
	Array.prototype.splice = function(index, howMany, elementN){}
	/**
	 * Adds one or more elements to the beginning of an array and returns the new length of the array.
	 * 
	 * @param {Object} elementN The elements to add to the front of the array. 
	 * @returns {Number} 
	 */
	Array.prototype.unshift = function(elementN){}
	/**
	 * Returns the first index at which a given element can be found in the array, or -1 if it is not present.
	 * 
	 * @param {Object} searchElement Element to locate in the array.
	 * @param {Number} fromIndex The index at which to begin the search. Defaults to 0, i.e. the whole array will be searched. If the index is greater than or equal to the length of the array, -1 is returned, i.e. the array will not be searched. If negative, it is taken as the offset from the end of the array. Note that even when the index is negative, the array is still searched from front to back. If the calculated index is less than 0, the whole array will be searched.
	 * @returns {Number} 
	 */
	Array.prototype.indexOf = function(searchElement, fromIndex){}
	/**
	 * Returns the last index at which a given element can be found in the array, or -1 if it is not present. The array is searched backwards, starting at fromIndex.
	 * 
	 * @param {Object} searchElement Element to locate in the array.
	 * @param {Number} fromIndex The index at which to start searching backwards. Defaults to the array's length, i.e. the whole array will be searched. If the index is greater than or equal to the length of the array, the whole array will be searched. If negative, it is taken as the offset from the end of the array. Note that even when the index is negative, the array is still searched from back to front. If the calculated index is less than 0, -1 is returned, i.e. the array will not be searched.
	 * @returns {Number} 
	 */
	Array.prototype.lastIndexOf = function(searchElement, fromIndex){}
	/**
	 * Tests whether all elements in the array pass the test implemented by the provided function.
	 * 
	 * @param {Function} callback Function to test for each element.
	 * @param {Object} thisObject Object to use as this when executing callback.
	 * @returns {Boolean} 
	 */
	Array.prototype.every = function(callback, thisObject){}
	/**
	 * Tests whether some element in the array passes the test implemented by the provided function.
	 * 
	 * @param {Function} callback Function to test for each element.
	 * @param {Object} thisObject Object to use as this when executing callback.
	 * @returns {Boolean} 
	 */
	Array.prototype.some = function(callback, thisObject){}
	/**
	 * Executes a provided function once per array element.
	 * 
	 * @param {Function} callback Function to execute for each element.
	 * @param {Object} thisObject Object to use as this when executing callback.
	 */
	Array.prototype.forEach = function(callback, thisObject){}
	/**
	 * Creates a new array with the results of calling a provided function on every element in this array.
	 * 
	 * @param {Function} callback Function that produces an element of the new Array from an element of the current one.
	 * @param {Object} thisObject Object to use as this when executing callback.
	 * @returns {Array} 
	 */
	Array.prototype.map = function(callback, thisObject){}
	/**
	 * Creates a new array with all elements that pass the test implemented by the provided function.
	 * 
	 * @param {Function} callback Function to test each element of the array.
	 * @param {Object} thisObject Object to use as this when executing callback.
	 * @returns {Array} 
	 */
	Array.prototype.filter = function(callback, thisObject){}
	/**
	 * Apply a function against an accumulator and each value of the array (from left-to-right) as to reduce it to a single value.
	 * 
	 * @param {Function} callback Function to execute on each value in the array.
	 * @param {Object} initialValue Object to use as the first argument to the first call of the callback.
	 * @returns {Array} 
	 */
	Array.prototype.reduce = function(callback, initialValue){}
	/**
	 * Apply a function simultaneously against two values of the array (from right-to-left) as to reduce it to a single value.
	 * 
	 * @param {Function} callback Function to execute on each value in the array.
	 * @param {Object} initialValue Object to use as the first argument to the first call of the callback.
	 * @returns {Array} 
	 */
	Array.prototype.reduceRight = function(callback, initialValue){}
}
// Array class: static properties and methods
/**
 * Returns true if an variable is an array, if not false.
 * 
 * @param {Object} obj The object to be checked
 * @static
 */
Array.isArray = function(obj){}

// Array class: prototype
Array.prototype = new Function();


/**
 * String class.
 * String is a global object that may be used to construct String instances.
 * 
 * @class String
 * @param {String} value Any string.
 * @returns {String} 
 */
function String(value) {
	// properties
	
	// methods
	/**
	 * Returns the specified character from a string.
	 * 
	 * @param {Number} index An integer between 0 and 1 less than the length of the string.
	 * @returns {String} 
	 */
	String.prototype.charAt = function(index){}
	/**
	 * Returns the numeric Unicode value of the character at the given index (except for unicode codepoints > 0x10000).
	 * 
	 * @param {Number} index An integer greater than 0 and less than the length of the string; if it is not a number, it defaults to 0.
	 * @returns {Number} 
	 */
	String.prototype.charCodeAt = function(index){}
	/**
	 * Combines the text of two or more strings and returns a new string.
	 * 
	 * @param {String} stringN Strings to concatenate to this string. 
	 * @returns {String} 
	 */
	String.prototype.concat = function(stringN){}
	/**
	 * Returns the index within the calling String object of the first occurrence of the specified value, starting the search at fromIndex,
	 * returns -1 if the value is not found.
	 * 
	 * @param {String} searchValue A string representing the value to search for.
	 * @param {Number} fromIndex The location within the calling string to start the search from. It can be any integer between 0 and the length of the string. The default value is 0.
	 * @returns {Number} 
	 */
	String.prototype.indexOf = function(searchValue, fromIndex){}
	/**
	 * Returns the index within the calling String object of the last occurrence of the specified value, or -1 if not found. The calling string is searched backward, starting at fromIndex.
	 * 
	 * @param {String} searchValue A string representing the value to search for. 
	 * @param {Number} fromIndex The location within the calling string to start the search from, indexed from left to right. It can be any integer between 0 and the length of the string. The default value is the length of the string. 
	 * @returns {Number} 
	 */
	String.prototype.lastIndexOf = function(searchValue, fromIndex){}
	/**
	 * Returns a number indicating whether a reference string comes before or after or is the same as the given string in sort order.
	 * 
	 * @param {String} compareString The string against which the referring string is comparing
	 * @returns {Number} 
	 */
	String.prototype.localeCompare = function(compareString){}
	/**
	 * Used to retrieve the matches when matching a string against a regular expression.
	 * 
	 * @param {RegExp} regexp A regular expression object. If a non-RegExp object obj is passed, it is implicitly converted to a RegExp by using new RegExp(obj).
	 * @returns {Array} 
	 */
	String.prototype.match = function(regexp){}
	/**
	 * Returns a new string with some or all matches of a pattern replaced by a replacement.  The pattern can be a string or a RegExp, and the replacement can be a string or a function to be called for each match.
	 * 
	 * @param {String|RegExp} searchValue <ul>
	 * <li>A RegExp object. The match is replaced by the return value of parameter #2.</li>
	 * <li>A String that is to be replaced by newSubStr.</li>
	 * </ul>
	 * @param {String|Function} replaceValue <ul>
	 * <li>The String that replaces the substring received from parameter #1. A number of special replacement patterns are supported; see the "Specifying a string as a parameter" section below.</li>
	 * <li>A function to be invoked to create the new substring (to put in place of the substring received from parameter #1). The arguments supplied to this function are described in the "Specifying a function as a parameter" section below.</li>
	 * </ul>
	 * @returns {String} 
	 */
	String.prototype.replace = function(searchValue, replaceValue){}
	/**
	 * Executes the search for a match between a regular expression and this String object.
	 * 
	 * @param {RegExp} regexp A regular expression object. If a non-RegExp object obj is passed, it is implicitly converted to a RegExp by using new RegExp(obj).
	 * @returns {Number} 
	 */
	String.prototype.search = function(regexp){}
	/**
	 * Extracts a section of a string and returns a new string.
	 * 
	 * @param {Number} beginSlice The zero-based index at which to begin extraction. 
	 * @param {Number} endSlice The zero-based index at which to end extraction. If omitted, slice extracts to the end of the string. 
	 * @returns {String} 
	 */
	String.prototype.slice = function(beginSlice, endSlice){}
	/**
	 * Splits a String object into an array of strings by separating the string into substrings.
	 * 
	 * @param {String} separator Specifies the character to use for separating the string. The separator is treated as a string or a regular expression. If separator is omitted, the array returned contains one element consisting of the entire string.
	 * @param {Number} limit Integer specifying a limit on the number of splits to be found.
	 * @returns {Array} 
	 */
	String.prototype.split = function(separator, limit){}
	/**
	 * Returns a subset of a string between one index and another, or through the end of the string.
	 * 
	 * @param {Number} indexA An integer between 0 and one less than the length of the string.
	 * @param {Number} indexB (optional) An integer between 0 and the length of the string.
	 * @returns {String} 
	 */
	String.prototype.substring = function(indexA, indexB){}
	/**
	 * Returns the calling string value converted to lower case, according to any locale-specific case mappings.
	 * 
	 * @returns {String} 
	 */
	String.prototype.toLocaleLowerCase = function(){}
	/**
	 * Returns the calling string value converted to upper case, according to any locale-specific case mappings.
	 * 
	 * @returns {String} 
	 */
	String.prototype.toLocaleUpperCase = function(){}
	/**
	 * Returns the calling string value converted to lowercase.
	 * 
	 * @returns {String} 
	 */
	String.prototype.toLowerCase = function(){}
	/**
	 * Returns the calling string value converted to uppercase.
	 * 
	 * @returns {String} 
	 */
	String.prototype.toUpperCase = function(){}
	/**
	 * Removes whitespace from both ends of the string.
	 * 
	 * @returns {String} 
	 */
	String.prototype.trim = function(){}
}
// String class: static properties and methods
/**
 * Returns a string created by using the specified sequence of Unicode values.
 * 
 * @param {Number} numN A sequence of numbers that are Unicode values.
 * @static
 */
String.fromCharCode = function(numN){}

// String class: prototype
String.prototype = new Function();


/**
 * Boolean class.
 * The Boolean object is an object wrapper for a boolean value.
 * 
 * @class Boolean
 * @param {Boolean} value The initial value of the Boolean object.
 * @returns {Boolean} 
 */
function Boolean(value) {
	// properties
	
	// methods

}
// Boolean class: static properties and methods

// Boolean class: prototype
Boolean.prototype = new Function();


/**
 * Number class.
 * Creates a wrapper object to allow you to work with numerical values.
 * 
 * @class Number
 * @param {Number} value The numeric value of the object being created.
 * @returns {Number} 
 */
function Number(value) {
	// properties
	
	// methods
	/**
	 * Returns a string representing the specified Number object
	 * 
	 * @param {Number} radix An integer between 2 and 36 specifying the base to use for representing numeric values. 
	 * @returns {String} 
	 */
	Number.prototype.toString = function(radix){}
	/**
	 * Formats a number using fixed-point notation
	 * 
	 * @param {Number} digits The number of digits to appear after the decimal point; this may be a value between 0 and 20, inclusive, and implementations may optionally support a larger range of values. If this argument is omitted, it is treated as 0. 
	 * @returns {Number} 
	 */
	Number.prototype.toFixed = function(digits){}
	/**
	 * Returns a string representing the Number object in exponential notation
	 * 
	 * @param {Number} fractionDigits An integer specifying the number of digits after the decimal point. Defaults to as many digits as necessary to specify the number. 
	 * @returns {Number} 
	 */
	Number.prototype.toExponential = function(fractionDigits){}
	/**
	 * Returns a string representing the Number object to the specified precision.
	 * 
	 * @param {Number} precision An integer specifying the number of significant digits.
	 * @returns {Number} 
	 */
	Number.prototype.toPrecision = function(precision){}
}
// Number class: static properties and methods
/**
 * The maximum numeric value representable in JavaScript. 
 * 
 * @property {Number} MAX_VALUE The maximum numeric value representable in JavaScript. 
 * @constant
 */
Number.MAX_VALUE = 1;
/**
 * The smallest positive numeric value representable in JavaScript.
 * 
 * @property {Number} MAX_VALUE The smallest positive numeric value representable in JavaScript.
 * @constant
 */
Number.MIN_VALUE = 1;
/**
 * A value representing Not-A-Number. 
 * 
 * @property {Object} NaN A value representing Not-A-Number. 
 * @constant
 */
Number.NaN = NaN;
/**
 * A value representing the negative Infinity value.
 * 
 * @property {Object} NEGATIVE_INFINITY A value representing the negative Infinity value.
 * @constant
 */
Number.NEGATIVE_INFINITY = Infinity;
/**
 * A value representing the positive Infinity value.
 * 
 * @property {Object} POSITIVE_INFINITY A value representing the positive Infinity value.
 * @constant
 */
Number.POSITIVE_INFINITY = Infinity;

// Number class: prototype
Number.prototype = new Function();


/**
 * Math class.
 * A built-in object that has properties and methods for mathematical constants and functions.
 * 
 * @class Math
 * @returns {Math} 
 */
function Math() {
	// properties
	
	// methods

}
// Math class: static properties and methods
/**
 * The base of natural logarithms, e, approximately 2.718.
 * 
 * @property {Number} E The base of natural logarithms, e, approximately 2.718.
 * @constant
 */
Math.E = 1;
/**
 * The natural logarithm of 10, approximately 2.302. 
 * 
 * @property {Number} LN10 The natural logarithm of 10, approximately 2.302. 
 * @constant
 */
Math.LN10 = 1;
/**
 * The natural logarithm of 2, approximately 0.693. 
 * 
 * @property {Number} LN2 The natural logarithm of 2, approximately 0.693. 
 * @constant
 */
Math.LN2 = 1;
/**
 * The base 10 logarithm of E (approximately 0.434). 
 * 
 * @property {Number} LOG10E The base 10 logarithm of E (approximately 0.434). 
 * @constant
 */
Math.LOG10E = 1;
/**
 * The base 2 logarithm of E (approximately 1.442). 
 * 
 * @property {Number} LOG2E The base 2 logarithm of E (approximately 1.442). 
 * @constant
 */
Math.LOG2E = 1;
/**
 * The ratio of the circumference of a circle to its diameter, approximately 3.14159. 
 * 
 * @property {Number} PI The ratio of the circumference of a circle to its diameter, approximately 3.14159. 
 * @constant
 */
Math.PI = 1;
/**
 * The square root of 1/2; equivalently, 1 over the square root of 2, approximately 0.707.
 * 
 * @property {Number} SQRT1_2 The square root of 1/2; equivalently, 1 over the square root of 2, approximately 0.707.
 * @constant
 */
Math.SQRT1_2 = 1;
/**
 * The square root of 2, approximately 1.414. 
 * 
 * @property {Number} SQRT2 The square root of 2, approximately 1.414. 
 * @constant
 */
Math.SQRT2 = 1;
/**
 * Returns the absolute value of a number.
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.abs = function(x){}
/**
 * Returns the arccosine (in radians) of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.acos = function(x){}
/**
 * Returns the arcsine (in radians) of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.asin = function(x){}
/**
 * Returns the arctangent (in radians) of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.atan = function(x){}
/**
 * Returns the arctangent of the quotient of its arguments. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.atan2 = function(x){}
/**
 * Returns the smallest integer greater than or equal to a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.ceil = function(x){}
/**
 * Returns the smallest integer greater than or equal to a number. 
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.ceil = function(n){}
/**
 * Returns the cosine of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.cos = function(x){}
/**
 * Returns E^x, where x is the argument, and E is Euler's constant, the base of the natural logarithms. 
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.exp = function(n){}
/**
 * Returns the largest integer less than or equal to a number. 
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.floor = function(n){}
/**
 * Returns the natural logarithm (base E) of a number. 
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.log = function(n){}
/**
 * Returns the largest of zero or more numbers.
 * 
 * @param {Number} x A number. 
 * @param {Number} y A number. 
 * @returns {Number} 
 * @static
 */
Math.max = function(x, y){}
/**
 * Returns the smallest of zero or more numbers. 
 * 
 * @param {Number} x A number. 
 * @param {Number} y A number. 
 * @returns {Number} 
 * @static
 */
Math.min = function(x, y){}
/**
 * Returns a pseudo-random number in the range [0,1) — that is, between 0 (inclusive) and 1 (exclusive). The random number generator is seeded from the current time, as in Java. 
 * 
 * @returns {Number} 
 * @static
 */
Math.random = function(){}
/**
 * Returns the value of a number rounded to the nearest integer.
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.round = function(n){}
/**
 * Returns the sine of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.sin = function(x){}
/**
 * Returns the square root of a number. 
 * 
 * @param {Number} n A number. 
 * @returns {Number} 
 * @static
 */
Math.sqrt = function(n){}
/**
 * Returns the tangent of a number. 
 * 
 * @param {Number} x A number. 
 * @returns {Number} 
 * @static
 */
Math.tan = function(x){}

// Math class: prototype
Math.prototype = new Function();


/**
 * Date class.
 * Creates Date instances which let you work with dates and times.
 * 
 * @class Date
 * @param {Number} year Integer value representing the year. For compatibility (in order to avoid the Y2K problem), you should always specify the year in full; use 1998, rather than 98.
 * @param {Number} month Integer value representing the month, beginning with 0 for January to 11 for December.
 * @param {Number} date Integer value representing the day of the month (1-31).
 * @param {Number} hours Integer value representing the hour of the day (0-23).
 * @param {Number} minutes Integer value representing the minute segment (0-59) of a time reading.
 * @param {Number} seconds Integer value representing the second segment (0-59) of a time reading.
 * @param {Number} ms Integer value representing the millisecond segment (0-999) of a time reading.
 * @returns {Date} 
 */
function Date(year, month, date, hours, minutes, seconds, ms) {
	// properties
	
	// methods
	/**
	 * Returns the numeric value corresponding to the time for the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getTime = function(){}
	/**
	 * Returns the year of the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getFullYear = function(){}
	/**
	 * Returns the month in the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getMonth = function(){}
	/**
	 * Returns the day of the month for the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getDate = function(){}
	/**
	 * Returns the hour for the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getHours = function(){}
	/**
	 * Returns the minutes in the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getMinutes = function(){}
	/**
	 * Returns the seconds in the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getSeconds = function(){}
	/**
	 * Returns the milliseconds in the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getMilliseconds = function(){}
	/**
	 * Returns the time-zone offset from UTC, in minutes, for the current locale.
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getTimezoneOffset = function(){}
	/**
	 * Sets the Date object to the time represented by a number of milliseconds since January 1, 1970, 00:00:00 UTC. 
	 * 
	 * @param {Number} timeValue An integer representing the number of milliseconds since 1 January 1970, 00:00:00 UTC. 
	 */
	Date.prototype.setTime = function(timeValue){}
	/**
	 * Sets the milliseconds for a specified date according to local time. 
	 * 
	 * @param {Number} timeValue A number between 0 and 999, representing the milliseconds.  
	 */
	Date.prototype.setMilliseconds = function(timeValue){}
	/**
	 * Sets the seconds for a specified date according to local time. 
	 * 
	 * @param {Number} secondsValue An integer between 0 and 59. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. 
	 */
	Date.prototype.setSeconds = function(secondsValue, msValue){}
	/**
	 * Sets the minutes for a specified date according to local time. 
	 * 
	 * @param {Number} minutesValue An integer between 0 and 59, representing the minutes. 
	 * @param {Number} secondsValue An integer between 0 and 59, representing the seconds. If you specify the secondsValue parameter, you must also specify the minutesValue. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. If you specify the msValue parameter, you must also specify the minutesValue and secondsValue.
	 */
	Date.prototype.setMinutes = function(minutesValue, secondsValue, msValue){}
	/**
	 * Sets the hours for a specified date according to local time. 
	 * 
	 * @param {Number} hoursValue An integer between 0 and 23, representing the hour. 
	 * @param {Number} minutesValue An integer between 0 and 59, representing the minutes. 
	 * @param {Number} secondsValue An integer between 0 and 59, representing the seconds. If you specify the secondsValue parameter, you must also specify the minutesValue. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. If you specify the msValue parameter, you must also specify the minutesValue and secondsValue.
	 */
	Date.prototype.setHours = function(hoursValue, minutesValue, secondsValue, msValue){}
	/**
	 * Sets the day of the month for a specified date according to local time. 
	 * 
	 * @param {Number} dayValue An integer from 1 to 31, representing the day of the month. 
	 */
	Date.prototype.setDate = function(dayValue){}
	/**
	 * Set the month for a specified date according to local time. 
	 * 
	 * @param {Number} monthValue An integer between 0 and 11 (representing the months January through December). 
	 * @param {Number} dayValue An integer from 1 to 31, representing the day of the month. 
	 */
	Date.prototype.setMonth = function(monthValue, dayValue){}
	/**
	 * Sets the full year for a specified date according to local time. 
	 * 
	 * @param {Number} yearValue An integer specifying the numeric value of the year, for example, 1995. 
	 * @param {Number} monthValue An integer between 0 and 11 (representing the months January through December). 
	 * @param {Number} dayValue An integer between 1 and 31 representing the day of the month. If you specify the dayValue parameter, you must also specify the monthValue. 
	 */
	Date.prototype.setFullYear = function(yearValue, monthValue, dayValue){}
	/**
	 * Converts a date to a string, using the universal time convention.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toUTCString = function(){}
	/**
	 * Returns the date portion of a Date object in human readable form in American English.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toDateString = function(){}
	/**
	 * Returns the time portion of a Date object in human readable form in American English.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toTimeString = function(){}
	/**
	 * Converts a date to a string, returning the "date" portion using the operating system's locale's conventions. 
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toLocaleDateString = function(){}
	/**
	 * Converts a date to a string, returning the "time" portion using the current locale's conventions.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toLocaleTimeString = function(){}
	/**
	 * Returns the year in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCFullYear = function(){}
	/**
	 * Returns the month of the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCMonth = function(){}
	/**
	 * Returns the day (date) of the month in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCDate = function(){}
	/**
	 * Returns the day of the week for the specified date according to local time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getDay = function(){}
	/**
	 * Returns the day of the week in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCDay = function(){}
	/**
	 * Returns the hours in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCHours = function(){}
	/**
	 * Returns the minutes in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCMinutes = function(){}
	/**
	 * Returns the seconds in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCSeconds = function(){}
	/**
	 * Returns the milliseconds in the specified date according to universal time. 
	 * 
	 * @returns {Number} 
	 */
	Date.prototype.getUTCMilliseconds = function(){}
	/**
	 * Sets the milliseconds for a specified date according to universal time. 
	 * 
	 * @param {Number} millisecondsValue A number between 0 and 999, representing the milliseconds. 
	 */
	Date.prototype.setUTCMilliseconds = function(millisecondsValue){}
	/**
	 * Sets the seconds for a specified date according to universal time. 
	 * 
	 * @param {Number} secondsValue An integer between 0 and 59. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. 
	 */
	Date.prototype.setUTCSeconds = function(secondsValue, msValue){}
	/**
	 * Sets the minutes for a specified date according to universal time. 
	 * 
	 * @param {Number} minutesValue An integer between 0 and 59, representing the minutes. 
	 * @param {Number} secondsValue An integer between 0 and 59, representing the seconds. If you specify the secondsValue parameter, you must also specify the minutesValue. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. If you specify the msValue parameter, you must also specify the minutesValue and secondsValue. 
	 */
	Date.prototype.setUTCMinutes = function(minutesValue, secondsValue, msValue){}
	/**
	 * Sets the hour for a specified date according to universal time. 
	 * 
	 * @param {Number} hoursValue An integer between 0 and 23, representing the hour. 
	 * @param {Number} minutesValue An integer between 0 and 59, representing the minutes. 
	 * @param {Number} secondsValue An integer between 0 and 59, representing the seconds. If you specify the secondsValue parameter, you must also specify the minutesValue. 
	 * @param {Number} msValue A number between 0 and 999, representing the milliseconds. If you specify the msValue parameter, you must also specify the minutesValue and secondsValue. 
	 */
	Date.prototype.setUTCHours = function(hoursValue, minutesValue, secondsValue, msValue){}
	/**
	 * Sets the day of the month for a specified date according to universal time. 
	 * 
	 * @param {Number} dayValue An integer from 1 to 31, representing the day of the month. 
	 */
	Date.prototype.setUTCDate = function(dayValue){}
	/**
	 * Sets the month for a specified date according to universal time. 
	 * 
	 * @param {Number} monthValue An integer between 0 and 11, representing the months January through December. 
	 * @param {Number} dayValue An integer from 1 to 31, representing the day of the month. 
	 */
	Date.prototype.setUTCMonth = function(monthValue, dayValue){}
	/**
	 * Sets the full year for a specified date according to universal time. 
	 * 
	 * @param {Number} yearValue An integer specifying the numeric value of the year, for example, 1995. 
	 * @param {Number} monthValue An integer between 0 and 11, representing the months January through December. 
	 * @param {Number} dayValue An integer between 1 and 31 representing the day of the month. If you specify the dayValue parameter, you must also specify the monthValue. 
	 */
	Date.prototype.setUTCFullYear = function(yearValue, monthValue, dayValue){}
	/**
	 * Converts a date to a string, using the universal time convention.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toISOString = function(){}
	/**
	 * Returns a JSON representation of the Date object.
	 * 
	 * @returns {String} 
	 */
	Date.prototype.toJSON = function(){}
}
// Date class: static properties and methods
/**
 * Returns the number of milliseconds elapsed since 1 January 1970 00:00:00 UTC.
 * 
 * @returns {Date} 
 * @static
 */
Date.now = function(){}
/**
 * Parses a string representation of a date, and returns the number of milliseconds since January 1, 1970, 00:00:00 UTC.
 * 
 * @param {String} dateString A string representing a date.
 * @returns {Date} 
 * @static
 */
Date.parse = function(dateString){}
/**
 * Accepts the same parameters as the longest form of the constructor, and returns the number of milliseconds in a Date object since January 1, 1970, 00:00:00, universal time. 
 * 
 * @param {String} year A year after 1900. 
 * @param {String} month An integer between 0 and 11 representing the month. 
 * @param {String} date An integer between 1 and 31 representing the day of the month. 
 * @param {String} hrs An integer between 0 and 23 representing the hours. 
 * @param {String} min An integer between 0 and 59 representing the minutes. 
 * @param {String} sec An integer between 0 and 59 representing the seconds. 
 * @param {String} ms An integer between 0 and 999 representing the milliseconds. 
 * @returns {Date} 
 * @static
 */
Date.UTC = function(year, month, date, hrs, min, sec, ms){}

// Date class: prototype
Date.prototype = new Function();


/**
 * RegExp class.
 * RegExp is a global object that may be used to construct RegExp instances.
 * 
 * @class RegExp
 * @param {String} pattern The text of the regular expression.
 * @param {String} flags If specified, flags can have any combination of the following values:<ul>
 * <li>g: global match</li>
 * <li>i: ignore case</li>
 * <li>m: Treat beginning and end characters (^ and $) as working over multiple lines (i.e., match the beginning or end of each line (delimited by \n or \r), not only the very beginning or end of the whole input string) </li>
 * </ul>
 * @returns {RegExp} 
 */
function RegExp(pattern, flags) {
	// properties
	/**
	 * A read-only property that contains the text of the pattern, excluding the forward slashes. 
	 * 
	 * @property {String} source A read-only property that contains the text of the pattern, excluding the forward slashes. 
	 */
	this.source = "";
	/**
	 * Whether or not the "g" flag is used with the regular expression. 
	 * 
	 * @property {Boolean} global Whether or not the "g" flag is used with the regular expression. 
	 */
	this.global = true;
	/**
	 * Whether or not the "i" flag is used with the regular expression. 
	 * 
	 * @property {Boolean} ignoreCase Whether or not the "i" flag is used with the regular expression. 
	 */
	this.ignoreCase = true;
	/**
	 * Reflects whether or not to search in strings across multiple lines. 
	 * 
	 * @property {Boolean} multiline Reflects whether or not to search in strings across multiple lines. 
	 */
	this.multiline = true;
	/**
	 * A read/write integer property that specifies the index at which to start the next match. 
	 * 
	 * @property {Number} lastIndex A read/write integer property that specifies the index at which to start the next match. 
	 */
	this.lastIndex = 1;
	// methods
	/**
	 * Executes a search for a match in a specified string. Returns a result array, or null.
	 * 
	 * @param {String} str The string against which to match the regular expression.
	 * @returns {Array} 
	 */
	RegExp.prototype.exec = function(str){}
	/**
	 * Executes the search for a match between a regular expression and a specified string. Returns true or false.
	 * 
	 * @param {String} str The string against which to match the regular expression.
	 * @returns {Boolean} 
	 */
	RegExp.prototype.test = function(str){}
}
// RegExp class: static properties and methods

// RegExp class: prototype
RegExp.prototype = new Function();


/**
 * Error class.
 * Creates an error object.
 * 
 * @class Error
 * @param {String} message Human-readable description of the error
 * @returns {Error} 
 */
function Error(message) {
	// properties
	/**
	 * A human-readable description of the error. 
	 * 
	 * @property {String} message A human-readable description of the error. 
	 */
	this.message = "";
	/**
	 * A name for the type of error. 
	 * 
	 * @property {String} name A name for the type of error. 
	 */
	this.name = "";
	// methods

}
// Error class: static properties and methods

// Error class: prototype


/**
 * EvalError class.
 * Represents an error regarding the eval function.
 * 
 * @class EvalError
 * @param {String} message Human-readable description of the error
 * @returns {EvalError} 
 */
function EvalError(message) {
	// properties

	// methods

}
// EvalError class: static properties and methods

// EvalError class: prototype
EvalError.prototype = new Error();


/**
 * RangeError class.
 * Represents an error when a number is not within the correct range allowed.
 * 
 * @class RangeError
 * @param {String} message Human-readable description of the error
 * @returns {RangeError} 
 */
function RangeError(message) {
	// properties

	// methods

}
// RangeError class: static properties and methods

// RangeError class: prototype
RangeError.prototype = new Error();


/**
 * ReferenceError class.
 * Represents an error when a non-existent variable is referenced.
 * 
 * @class ReferenceError
 * @param {String} message Human-readable description of the error
 * @returns {ReferenceError} 
 */
function ReferenceError(message) {
	// properties

	// methods

}
// ReferenceError class: static properties and methods

// ReferenceError class: prototype
ReferenceError.prototype = new Error();


/**
 * SyntaxError class.
 * Represents an error when trying to interpret syntactically invalid code.
 * 
 * @class SyntaxError
 * @param {String} message Human-readable description of the error
 * @returns {SyntaxError} 
 */
function SyntaxError(message) {
	// properties

	// methods

}
// SyntaxError class: static properties and methods

// SyntaxError class: prototype
SyntaxError.prototype = new Error();


/**
 * TypeError class.
 * Represents an error when a value is not of the expected type.
 * 
 * @class TypeError
 * @param {String} message Human-readable description of the error
 * @returns {TypeError} 
 */
function TypeError(message) {
	// properties

	// methods

}
// TypeError class: static properties and methods

// TypeError class: prototype
TypeError.prototype = new Error();


/**
 * URIError class.
 * Represents an error when a malformed URI is encountered.
 * 
 * @class URIError
 * @param {String} message Human-readable description of the error
 * @returns {URIError} 
 */
function URIError(message) {
	// properties

	// methods

}
// URIError class: static properties and methods

// URIError class: prototype
URIError.prototype = new Error();


/**
 * JSON class.
 * Creates an error object.
 * 
 * @class JSON
 * @returns {JSON} 
 */
function JSON() {
	// properties

	// methods

}
// JSON class: static properties and methods
/**
 * parses a JSON text (a JSON-formatted String) and produces an ECMAScript value
 * 
 * @param {String} text a JSON-formatted String
 * @param {Function} reviver It can filter and transform the results.
 * @returns {Object} 
 */
JSON.parse = function(text, reviver){}
/**
 * parses a JSON text (a JSON-formatted String) and produces an ECMAScript value
 * 
 * @param {Object} value an ECMAScript value
 * @param {Function|Array} replacer a function that alters the way objects and arrays are stringified, or an array of Strings and Numbers that acts as a white list for selecting the object properties that will be stringified.
 * @param {Number|String} space a String or Number that allows the result to have white space injected into it to improve human readability
 * @returns {String} 
 */
JSON.stringify = function(value, replacer, space){}

// JSON class: prototype

/**
 * Arguments class.
 * An array-like object corresponding to the arguments passed to a function.
 * 
 * @class Arguments
 * @returns {Arguments} 
 */
function Arguments() {
	// properties
	/**
	 * Specifies the currently executing function. 
	 * 
	 * @property {Function} callee Specifies the currently executing function. 
	 */
	 this.callee = function(){}

	// methods

}
// JSON class: static properties and methods

// JSON class: prototype
Arguments.prototype = new Array();


