public class ShapeClassifier {
	private int badGuesses; 
	private String[] threeParamGuesses = {"Equilateral", "Isosceles", "Scalene"};
	private String[] fourParamGuesses = {"Rectangle", "Square"};
	private String[] twoParamGuesses = {"Circle", "Ellipse", "Line"};

	public ShapeClassifier() {
		badGuesses = 0;
	}

	// return Yes if the guess is correct, No otherwise
	public String evaluateGuess(String arg) {

		String shapeGuessResult = "";
		Integer[] parameters = getParams(arg);
		String shapeGuess = getShapeGuess(arg);
		String sizeGuess = getSizeGuess(arg);
		String evenOddGuess = getEvenOddGuess(arg);
		int calcPerim = 0;

		if (shapeGuess == null)
			shapeGuess = "";

		if (sizeGuess == null)
			sizeGuess = "";

		if (evenOddGuess == null)
			evenOddGuess = "";


		switch (parameters.length) {
		case 1:
			if (shapeGuess.equals("Line")) {
				shapeGuessResult = shapeGuess;
				calcPerim = parameters[0];
			}
			break; 
		case 2: 
			shapeGuessResult = classify2Parameters(parameters[1], parameters[1]);
			if (shapeGuessResult.equals("Ellipse")) {
				calcPerim = calculateEllipsePerimeter(parameters[0],parameters[1]);
			}
			else {
				calcPerim = calculateCirclePerimeter(parameters[0]);
			}
			break;
		case 3:
			shapeGuessResult = classify3Parameters(parameters[0], parameters[1],parameters[2]);
			calcPerim = calculateTrianglePerimeter(parameters[1], parameters[1],parameters[2]);
			break;
		case 4:
			shapeGuessResult = classify4Parameters(parameters[0], parameters[1],parameters[2], parameters[3]);
			if (shapeGuessResult.equals("Rectangle")) {
				calcPerim = calculateRectanglePerimeter(parameters[0], parameters[3],parameters[2], parameters[3]);
			}
			else {
				calcPerim = calculateRectanglePerimeter(parameters[0], parameters[1],parameters[2], parameters[3]);
			}
		}

		Boolean isShapeGuessCorrect = null;
		Boolean isSizeGuessCorrect = null;
		Boolean isEvenOddCorrect = null;

		// check the shape guess
		if (shapeGuessResult.equals(shapeGuess))
			isShapeGuessCorrect = true;
		else 
			isShapeGuessCorrect = false;

		// check the size guess

		if (calcPerim > 200 && sizeGuess.equals("Large")) {
			isSizeGuessCorrect = true;
		}
		else if (calcPerim < 10 && sizeGuess.equals("Small")) {
			isSizeGuessCorrect = true;	
		}
		else { 
			isSizeGuessCorrect = false;
		}

		if ( 0 == (calcPerim % 2) && evenOddGuess.equals("Yes")) {
			isEvenOddCorrect = true;
		}
		else if ( 0 != (calcPerim % 2) && evenOddGuess.equals("No")) {
			isEvenOddCorrect = true;
		}
		else { 
			isEvenOddCorrect = false;
		}

		if (isShapeGuessCorrect && isSizeGuessCorrect && isEvenOddCorrect) {
			badGuesses=0;
			return "Yes";
		}
		else {
			// too many bad guesses
			badGuesses++;
			if (badGuesses >= 3) {
				System.out.println("Bad guess limit Exceeded");
				System.exit(1);
			}
			return "No";
		}
	}

	// P = 2 * PI *r
	private int calculateCirclePerimeter(int r) {
		return (int) (2 * Math.PI * r);
	}

	// P = 4 * s
	private int calculateSquarePerimeter(int side) {
		return 4 * side;
	}

	// P = 2l + 2w)
	private int calculateRectanglePerimeter(int side1, int side2, int side3, int side4) {
		if (side1 == side2) {

			return (2 * side1) + (2 * side3);
		} 

		else if (side2 == side3) {
			return (2 * side1) + (2 * side2);
		}

		return 0;
	}

	// P = a + b + c
	private int calculateTrianglePerimeter(int side1, int side2 , int side3) {
		return side1 + side2 + side3;
	}

	// This is an approximation
	// PI(3(a+b) - sqrt((3a+b)(a+3b))
	private int calculateEllipsePerimeter(int a, int b) {
		double da = a, db = b;
		return (int) ((int) Math.PI * (3 * (da+db) - Math.sqrt((3*da+db)*(da+3*db)))); 
	}

	// Transform a string argument into an array of numbers
	private Integer[] getParams(String args) {
		String[] params = args.split(",");
		Integer[] numParams = null;

		numParams = new Integer[params.length-3];
		for (int i=3; i<params.length; i++) {
			numParams[i-3] = Integer.parseInt(params[i]);
		}
		return numParams;		
	}

	// extract the Even/Odd guess
	private String getEvenOddGuess(String args) {
		String[] params = args.split(",");
		return params[2];		
	}

	// extract the size guess
	private String getSizeGuess(String args) {
		String[] params = args.split(",");
		return params[1];		
	}

	// extract the shape guess 
	private String getShapeGuess(String args) {
		String[] params = args.split(",");
		return params[0];
	}

	// classify an two sides 
	private String classify2Parameters(int a, int b) {
		if (a == b) {
			return twoParamGuesses[0];
		}
		else if (a == 0) {
			if (b > 0) {
				return twoParamGuesses[1];
			}
		}
		else if (a > 1) {
			if (b != 0) {
				return twoParamGuesses[1]; 
			}
		}
		return "";
	}

	// Classify four sides
	private String classify4Parameters(int a, int b, int c, int d) {
		if (a == b && c == d) {
			if (a != c) {
				return fourParamGuesses[1];
			}
			else 
				return fourParamGuesses[0];
		}		
		else if (b == d && c == a) {
			return fourParamGuesses[0];
		}
		else if (b == c && a == d) {
			return fourParamGuesses[0];
		}
		return  "";
	}

	// Classify a triangle based on the length of its sides
	private String classify3Parameters(int a, int b, int c) {

		if ( (a < (b+c)) && (b < (a + c)) && (c < (a+b))) {
			if (a == b && b == c) {
				return threeParamGuesses[0]; // Equilateral
			} else if (a!=b && a!=c && b!=c) {
				return threeParamGuesses[2]; // Scalene
			} else {
				return threeParamGuesses[1]; // Isosceles
			}  
		}
		return "";
	}
}

