import className from "classnames";
import { FunctionComponent } from "react";

type TagProps = React.HTMLAttributes<HTMLDivElement> &
  Partial<{
    text?: string;
    colour?: string
  }>;;

const Button: FunctionComponent<TagProps> = ({ colour, children, ...rest }) => {

  const classes = className(
    `border py-0.5 px-1 text-xs bg-[#${colour}] rounded`,
    rest.className,
  );

  return (
    <span {...rest} className={classes}>
      {children}
    </span>
  );
}

export default Button;