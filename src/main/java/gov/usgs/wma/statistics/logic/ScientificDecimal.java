package gov.usgs.wma.statistics.logic;

import java.math.BigDecimal;

/**
 * A more proper implementation of significant figures.
 * The BigDecimal counts 1000 as having 4 figures when it only has 1.
 * Also, 0.0000 is reported as 1 even though it has at least 4.
 * 
 * This implementation removes leading zeros and corrects for the 
 * presence of the decimal point in zero and integers. While a stand 
 * alone 1000 only has 1 sig fig, it is my experience from college 
 * science classes that 1000. has 4 figures. Note that there is no 
 * way to report 1000 with 2 or 3 figures. Because of this there is a 
 * constructor to set the known precision.
 * 
 * Should we discover that this assertion was a singular professor
 * interpretation, we can revisit the rules.
 * 
 * new ScientificDecimal("1000", 3) will create set 3 figures.
 * new ScientificDecimal("1000").setSigfigs(3) will also work.
 * 
 * @author duselman
 *
 */
@SuppressWarnings("serial")
public class ScientificDecimal extends BigDecimal {
	
	int sigfigs;

	public ScientificDecimal(String value, int sigfigs) {
		super(value);
		this.sigfigs = sigfigs;
	}

	public ScientificDecimal(String value) {
		super(value);
		sigfigRules(value);
	}
	
	 // TODO the proper handling of 0.000... will need to be revisited if my "guess" is incorrect.
	protected void sigfigRules(String value) {
		sigfigs = super.precision();
		
		// this is for 1000 vs 1000.
		if ( ! value.contains(".") ) {
			String figs = value.replaceAll("0+$", "");
			sigfigs = figs.length();
		}
		// this is for 0.0000 vs 0
		if ( equals( BigDecimal.ZERO.setScale(scale()) ) ) {
			sigfigs = scale();
		}
	}

	@Override
	public int precision() {
		return sigfigs;
	}
	
	public ScientificDecimal setSigfigs(int sigfigs) {
		this.sigfigs = sigfigs;
		return this;
	}
	public void setPrecision(int sigfigs) {
		this.setSigfigs(sigfigs);
	}

}
