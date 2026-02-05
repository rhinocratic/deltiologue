import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import TextArea from "./elements/TextArea";
import Section from "./elements/Section";
import FileUpload from "./elements/FileUpload";
import Select from "./elements/Select";
import FormFields from "./elements/FormFields";
import CheckBox from "./elements/CheckBox";
import Paragraph from "../text/Paragraph";
import FieldSet from "./elements/FieldSet";
import CheckBoxField from "./elements/CheckBoxField";
import Legend from "./elements/Legend";
import RadioButton from "./elements/RadioButton";
import RadioButtonGroup from "./elements/RadioButtonGroup";
import ColourPicker from "./elements/ColourPicker";


export default function FormExample() {

  return (
    <Form>
      <Section>
        <Title>Profile</Title>
        <Paragraph>This information will be displayed publicly so be careful what you share.</Paragraph>

        <FormFields>

          <div className="sm:col-span-1">
            <Label htmlFor="category-colour">Colour</Label>
            <ColourPicker id="category-colour" value={"aaaaff"} />
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="category-name">Name</Label>
            <TextField id="category-name" name="category-name" placeholder="Category Name" />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="username">Username</Label>
            <TextField id="username" name="username" placeholder="janesmith" prefix="workcation.com/" />
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">About</Label>
            <TextArea id="about" name="about" rows={3}></TextArea>
            <Paragraph>Write a few sentences about yourself.</Paragraph>
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">Image Upload</Label>
            <FileUpload />
          </div>
        </FormFields>
      </Section>

      <Section>
        <FormFields>
          <div className="sm:col-span-3">
            <Label htmlFor="first-name">First name</Label>
            <TextField id="first-name" name="first-name" />
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="last-name">Last name</Label>
            <TextField id="last-name" name="last-name" />
          </div>

          <div className="sm:col-span-4">
            <Label htmlFor="email">Email address</Label>
            <TextField id="email" name="last-name" />
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="country">Country</Label>
            <Select name="country">
              <option>Germany</option>
              <option>France</option>
              <option>Italy</option>
            </Select>
          </div>

          <div className="sm:col-span-full">
            <Label htmlFor="street-address">Street address</Label>
            <TextField id="street-address" name="street-address" />
          </div>

          <div className="sm:col-span-2 sm:col-start-1">
            <Label htmlFor="city">City</Label>
            <TextField id="city" name="city" />
          </div>

          <div className="sm:col-span-2">
            <Label htmlFor="region">State / Province</Label>
            <TextField id="region" name="region" />
          </div>

          <div className="sm:col-span-2">
            <Label htmlFor="postal-code">Zip / Postal code</Label>
            <TextField id="postal-code" name="postal-code" />
          </div>

        </FormFields>
      </Section>

      <Section>
        <Title>Notifications</Title>
        <Paragraph>
          We'll always let you know about important changes, but you pick what else you want to hear about.
        </Paragraph>

        <FieldSet>
          <Legend>By email</Legend>

          <CheckBoxField>
            <CheckBox id="comments" name="comments" />
            <Paragraph>
              <Label htmlFor="comments">Comments</Label>
              <Paragraph small>Get notified when someones posts a comment on a posting.</Paragraph>
            </Paragraph>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="candidates" name="candidates" />
            <Paragraph>
              <Label htmlFor="candidates">Candidates</Label>
              <Paragraph small>Get notified when a candidate applies for a job.</Paragraph>
            </Paragraph>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="offers" name="offers" />
            <Paragraph>
              <Label htmlFor="offers">Offers</Label>
              <Paragraph small>Get notified when a candidate accepts or rejects an offer.</Paragraph>
            </Paragraph>
          </CheckBoxField>
        </FieldSet>

        <FieldSet>
          <Legend>Push Notifications</Legend>
          <Paragraph>These are delivered via SMS to your mobile phone.</Paragraph>

          <RadioButtonGroup>
            <RadioButton id="push-everything" name="push-notifications" defaultChecked>
              <Label htmlFor="push-everything">
                Nothing
              </Label>
            </RadioButton>

            <RadioButton id="push-email" name="push-notifications">
              <Label htmlFor="push-email">
                Same as email
              </Label>
            </RadioButton>

            <RadioButton id="push-nothing" name="push-notifications">
              <Label htmlFor="push-nothing">
                No push notifications
              </Label>
            </RadioButton>

          </RadioButtonGroup>
        </FieldSet>

      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}