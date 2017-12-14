package com.google.refine.model.medadata.validator.checks;

import org.json.JSONObject;

import com.google.refine.model.Cell;
import com.google.refine.model.Project;
import com.google.refine.model.medadata.validator.ValidatorInspector;

import io.frictionlessdata.tableschema.Field;
import io.frictionlessdata.tableschema.exceptions.ConstraintsException;
import io.frictionlessdata.tableschema.exceptions.InvalidCastException;

public class TypeorFormatError extends AbstractValidator {
    private String type;
    private String format;
    
    public TypeorFormatError(Project project, int cellIndex, JSONObject options) {
        super(project, cellIndex, options);
        this.code = "type-or-format-error";
        this.type = options.getJSONObject(ValidatorInspector.CONSTRAINT_KEY).getString("type");
        this.format = options.getJSONObject(ValidatorInspector.CONSTRAINT_KEY_EXTRA).getString("format");
    }
    
    @Override
    public boolean checkCell(Cell cell) {
        boolean valid = true;
        
        try {
            Field field = project.getSchema().getField(project.columnModel.getColumnNames().get(cellIndex));
            field.castValue(cell.value.toString());
        } catch (InvalidCastException | ConstraintsException e) {
          valid = false;
        } 
            
        return valid;
    }
    
    @Override
    public void customizedFormat() {
        lookup.put("field_type", type);
        lookup.put("field_format", format);
    }
}