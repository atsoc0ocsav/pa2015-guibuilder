package pt.iscte.pidesco.guibuilder.internal;

public class GeneratorCode {

	public GeneratorCode(GeneratorCode.selectTarget target, String targetObject) {
		// Choose String targetObject or maybe ComponentInComposite components

		if (target.equals(selectTarget.SWING)) {
			SwingOjects swingObjects = new SwingOjects();

			switch (targetObject) {
			case "button":
				String codeButton = swingObjects.generateButton(null);
				// do something

				break;
			case "label":
				String codeLabel = swingObjects.generateButton(null);
				// do something
				break;
			case "textfield":
				String codeTextField = swingObjects.generateButton(null);
				// do something
				break;
			case "checkbox":
				String codeCheckBox = swingObjects.generateButton(null);
				// do something
				break;
			case "radiobutton":
				String codeRadioButton = swingObjects.generateButton(null);
				// do something
				break;
			default:
				break;
			}

		} else if (target.equals(selectTarget.SWI)) {
			SWIObjects swiObjects = new SWIObjects();

			switch (targetObject) {
			case "button":
				String codeButton = swiObjects.generateButton(null);
				// do something

				break;
			case "label":
				String codeLabel = swiObjects.generateButton(null);
				// do something
				break;
			case "textfield":
				String codeTextField = swiObjects.generateButton(null);
				// do something
				break;
			case "checkbox":
				String codeCheckBox = swiObjects.generateButton(null);
				// do something
				break;
			case "radiobutton":
				String codeRadioButton = swiObjects.generateButton(null);
				// do something
				break;
			default:
				break;
			}
		}

	}

	public enum selectTarget {

		SWING("swing"), SWI("swi");

		private String target;

		private selectTarget(String target) {
			this.target = target;
		}

		public String getTarget() {
			return target;
		}
	}

	private class SwingOjects implements GenerateObjectsInterface {

		@Override
		public String generateButton(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateLabel(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateTextField(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateRadioButton(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateAction(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private class SWIObjects implements GenerateObjectsInterface {

		@Override
		public String generateButton(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateLabel(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateTextField(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateRadioButton(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateAction(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
