package pa.iscde.guibuilder.ui;

import org.eclipse.swt.SWT;

public class GuiLabels {
	public enum Color {
		Black(SWT.COLOR_BLACK), Blue(SWT.COLOR_BLUE), Cyan(SWT.COLOR_CYAN), Grey(SWT.COLOR_GRAY), Green(
				SWT.COLOR_GREEN), Magenta(SWT.COLOR_MAGENTA), Red(SWT.COLOR_RED), White(SWT.COLOR_WHITE), Yellow(
						SWT.COLOR_YELLOW), Other(-1);

		private int swt_value;

		Color(int swt_value) {
			this.swt_value = swt_value;
		}

		public int swt_value() {
			return swt_value;
		}
	}

	public enum DialogMenuLabel {
		RENAME("Rename"), CHOOSE_BACKGROUND_COLOR("Choose Background Color"), CHOOSE_FOREGROUND_COLOR(
				"Choose Foreground Color"), GENERATE_CODE("Generate Code"), SET_WINDOW_TITLE("Set window title");

		private String str;

		private DialogMenuLabel(String str) {
			this.str = str;
		}

		public String str() {
			return str;
		}
	}

	public enum GUIBuilderObjectFamily {
		COMPONENTS("Components"), LAYOUTS("Layouts"), CONTAINERS("Containers"), CANVAS("Canvas");

		private String str;

		private GUIBuilderObjectFamily(String str) {
			this.str = str;
		}

		public String str() {
			return str;
		}
	}

	public enum GUIBuilderComponent {
		BTN("Button"), LABEL("Label"), TXTFIELD("Text Field"), RADIO_BTN("Radio Button"), CHK_BOX("Check Box"), WIDGET(
				"Widget");

		private String str;

		private GUIBuilderComponent(String str) {
			this.str = str;
		}

		public String str() {
			return str;
		}
	}

	public enum GUIBuilderLayout {
		// FLOW("Flow Layout"), ABSOLUTE("Absolute Layout"),
		// BORDER("Border Layout");
		FLOW("Flow Layout"), ABSOLUTE("Absolute Layout");

		private String str;

		private GUIBuilderLayout(String str) {
			this.str = str;
		}

		public String str() {
			return str;
		}
	}

	public enum GUIBuilderContainer {
		PANEL("Panel");

		private String str;

		private GUIBuilderContainer(String str) {
			this.str = str;
		}

		public String str() {
			return str;
		}
	}
}
