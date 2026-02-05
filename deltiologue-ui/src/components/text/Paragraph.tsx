import className from "classnames";
import { FunctionComponent } from "react";

type ParagraphProps = React.HTMLAttributes<HTMLDivElement> &
  Partial<{ small?: boolean }>;

const Paragraph: FunctionComponent<ParagraphProps> = ({ small, children, ...rest }) => {

  const classes = className(
    rest.className,
    small ? "text-sm/6" : "",
    "text-gray-500"
  );

  return (
    <p {...rest} className={classes}>
      {children}
    </p>
  );
}

export default Paragraph;