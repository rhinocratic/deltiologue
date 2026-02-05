import { FunctionComponent, useState } from "react";
import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import Section from "./elements/Section";
import FormFields from "./elements/FormFields";

type SeriesFormProps = {
  name?: string;
}

const SeriesForm: FunctionComponent<SeriesFormProps> = ({ name }) => {

  const [seriesName, setSeriesName] = useState(name);

  const handleSetName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSeriesName(event.target.value);
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Section>
        <Title>Create a new series</Title>
        <FormFields>
          <div className="sm:col-span-3">
            <Label htmlFor="series-name">Name</Label>
            <TextField
              id="series-name"
              name="series-name"
              placeholder="series name"
              value={seriesName}
              onChange={handleSetName}
            />
          </div>
        </FormFields>
      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}

export default SeriesForm;