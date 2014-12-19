/**
 * 
 */
package main;

/**
 * @author ngoduyhoa
 *
 */
public class YAMOptions 
{
	public	static String	SRC_ONTO_PATH;
	public	static String	TAR_ONTO_PATH;
	public	static String	ALIGN_OUTPUT_PATH;
	
	// Machine Learning Method
	public	static	boolean	ML_METHOD	=	false; // default: false
	
	// Information Retrieval Method
	public	static	boolean	IR_METHOD	=	true; // default: true

	// Instance-Based Method
	public	static	boolean	IB_METHOD	=	false; // default: true
	
	// Similarity Propagation Method
	public	static	boolean	SP_METHOD	=	true; // default: true
	
	// Semantic Verification Method
	public	static	boolean	SV_METHOD	=	true; // default: true
	
	public	static	boolean	EVALUATION	=	false; // default: false
	
	public	static	boolean	NOTRANSLATED	=	false; // default: false
}
