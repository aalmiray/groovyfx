/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package groovyx.javafx.factory

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.*
import groovyx.javafx.appsupport.Action

import static groovyx.javafx.factory.ActionFactory.extractActionParams
import static groovyx.javafx.factory.ActionFactory.applyAction

/**
 *
 * @author jimclarke
 */
class LabeledFactory extends AbstractNodeFactory {
    
    LabeledFactory(Class  beanClass) {
        super(beanClass);
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Action action = null
        Map actionParams = [:]
        if (value instanceof Action) {
            action = value
            value = null
            actionParams = extractActionParams(attributes)
        }

        def control  = super.newInstance(builder, name, value, attributes)

        if (control instanceof ButtonBase && action) {
            applyAction(control, action, actionParams)
        }

        if (value != null) {
            control.text = value.toString()
        }
        control
    }

    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (node instanceof ChoiceBox) {
            List items = attributes.remove("items");
            if (items) {
                if (!(items instanceof ObservableList)) {
                    items = FXCollections.observableArrayList(items)
                }

                node.setItems(items);
            }

        }
        return super.onHandleNodeAttributes(builder, node, attributes)
    }


    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Tooltip && parent instanceof Control) {
            ((Control) parent).setTooltip(child);
        } else if (child instanceof Node) {
            ((Labeled) parent).setGraphic(child);
        } else if (child instanceof ContextMenu) {
            parent.contextMenu = child;
        } else {
            super.setChild(builder, parent, child);
        }
    }
}
