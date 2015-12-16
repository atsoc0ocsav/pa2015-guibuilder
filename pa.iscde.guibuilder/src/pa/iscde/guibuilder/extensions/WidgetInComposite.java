package pa.iscde.guibuilder.extensions;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator;
import pa.iscde.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public abstract class WidgetInComposite extends ComponentInCompositeImpl {

	/**
	 * Existe um exemplo de como usar esta classe em
	 * https://github.com/atsoc0ocsav/pa2015-guibuilder/blob/master/pa.iscde.
	 * guibuilder.rudiluis/src/pa/iscde/guibuilder/rudiluis/SuperSpinner.java
	 */

	/**
	 * ContextMenuItem é um enumerado com as opcções disponíveis quando é usado
	 * o botão direito do rato No construtor é necessário chamar o super com as
	 * opcções desejadas, sendo o último elemento do array
	 * ContextMenuItem.PLUGIN exemplo super(new ContextMenuItem[] {
	 * ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });
	 * 
	 * @param contextMenuItems
	 */

	public WidgetInComposite(ContextMenuItem[] contextMenuItems) {
		super(GUIBuilderComponent.WIDGET, contextMenuItems);
	}

	/**
	 * Esta string será apresentada na TabBar
	 * 
	 * @return nome do widget
	 */
	public abstract String getWidgetName();

	/**
	 * Alterar o nome do widget apresentado no TabBar
	 * 
	 * @param widgetName
	 */

	public abstract void setWidgetName(String widgetName);

	/**
	 * Este método serve para criar o widget sendo também necessário fazer o
	 * update de variávies da super classe nomeadamente control, location, size,
	 * backgroundColor, foregroundColor e enabled
	 * 
	 * @param canvas
	 * @param location
	 * @param size
	 */

	public abstract void createWidget(Canvas canvas, Point location, Point size);

	/**
	 * The first line of generated code has to have the name of the widget
	 * variable Tem que ser gerado o código para SWING e SWT
	 * 
	 * @param target
	 *            CodeGenerator.CodeTarget é um enumerado tendo como conteudo
	 *            SWING e SWT
	 * @param containerName
	 *            é só para ser usado no contexto da geração de codigo para SWT, usado para definir um componente swt
	 * @param count
	 *            esta variável tem que estar junta com a variável do widget
	 * @return Lista com o código de um determinado target
	 */

	public abstract ArrayList<String> generateWidgetCode(CodeGenerator.CodeTarget target, String containerName,
			int count);
}
