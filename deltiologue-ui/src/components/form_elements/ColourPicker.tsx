import className from "classnames";
import { ColorPicker, ColorPickerProps } from "primereact/colorpicker";
import { FunctionComponent } from "react";

const ColourPicker: FunctionComponent<ColorPickerProps> = ({ ...rest }) => {

  const classes = className(
    "mt-2",
    rest.className,
  );

  return (
    <div className={classes}>
      <ColorPicker {...rest} />
    </div>
  );
}

export default ColourPicker;