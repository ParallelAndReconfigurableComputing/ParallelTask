package spoon.examples.factory.processing;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtNamedElement;

public class SearchProcessor extends AbstractProcessor<CtNamedElement> {

	public void process(CtNamedElement e) {
		// System.out.println(e.getClass().getSimpleName() + " "
		// + e.getSimpleName());
		if (e.getSimpleName().matches(searchString)) {
			getFactory().getEnvironment().report(this, Severity.MESSAGE, e,
					"found");
		}
	}

	public SearchProcessor() {
		super();
		readSearchString();
	}

	private String searchString;

	private void readSearchString() {
		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			System.out.println("enter search string:");
			searchString = br.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}