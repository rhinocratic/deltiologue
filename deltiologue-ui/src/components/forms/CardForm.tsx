import { FunctionComponent, useState } from "react";
import Button from "./elements/Button";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import Section from "./elements/Section";
import FormFields from "./elements/FormFields";
import FieldSet from "./elements/FieldSet";
import Legend from "./elements/Legend";
import CheckBoxField from "./elements/CheckBoxField";
import CheckBox from "./elements/CheckBox";
import Paragraph from "../text/Paragraph";
import TextArea from "./elements/TextArea";
import FileUpload from "./elements/FileUpload";

type Flags = {
  draft: boolean;
  rp: boolean;
  used: boolean;
  posted: boolean;
  franked: boolean;
  divided_back: boolean;
}

const defaultFlags = {
  draft: true,
  rp: false,
  used: false,
  posted: false,
  franked: false,
  divided_back: false,
};

type Location = {
  lat: number;
  lng: number;
}

const defaultLocation = {
  lat: 0.0,
  lng: 0.0
};

type Subject = {
  description: string;
  location: Location;
  current_view: string;
}

const defaultSubject = {
  description: "",
  location: defaultLocation,
  current_view: ""
};

type Recipient = {
  name: string;
  address: string;
}

const defaultRecipient = {
  name: "",
  address: ""
};

type Image = {
  id: number;
  alt: string;
}

const defaultImage = {
  id: 0,
  alt: ""
};

type Images = {
  front: Image;
  rear: Image;
}

const defaultImages = {
  front: defaultImage,
  rear: defaultImage
};

type Publisher = {
  id: number;
  name: string;
}

const defaultPublisher = {
  id: 0,
  name: ""
};

type Series = {
  id: number;
  name: string;
}

const defaultSeries = {
  id: 0,
  name: ""
};

type Card = {
  subject: Subject;
  flags: Flags;
  notes: string;
  recipient: Recipient;
  images: Images;
  publication_description: string;
  publisher: Publisher;
  series: Series;
}

const defaultCard = {
  subject: defaultSubject,
  flags: defaultFlags,
  notes: "",
  recipient: defaultRecipient,
  images: defaultImages,
  publication_description: "",
  publisher: defaultPublisher,
  series: defaultSeries,
};

type CardProps = {
  card?: Card;
}

const CardForm: FunctionComponent<CardProps> = ({ card }) => {

  const [cardState, setCardState] = useState({ ...defaultCard, ...card });
  const [description, setDescription] = useState(cardState.subject.description);
  const [latitude, setLatitude] = useState(cardState.subject.location.lat);
  const [longitude, setLongitude] = useState(cardState.subject.location.lng);
  const [currentView, setCurrentView] = useState(cardState.subject.current_view);
  const [cardFlags, setCardFlags] = useState(cardState.flags);
  const [notes, setNotes] = useState(cardState.notes);
  const [recipientName, setRecipientName] = useState(cardState.recipient.name);
  const [recipientAddress, setRecipientAddress] = useState(cardState.recipient.address);
  const [frontImage, setFrontImage] = useState(cardState.images.front.id);
  const [frontImageAltText, setFrontImageAltText] = useState(cardState.images.front.alt);
  const [rearImage, setRearImage] = useState(cardState.images.rear.id);
  const [rearImageAltText, setRearImageAltText] = useState(cardState.images.rear.alt);
  const [publicationDescription, setPublicationDescription] = useState(cardState.publication_description);
  const [publisher, setPublisher] = useState(cardState.publisher);
  const [series, setSeries] = useState(cardState.series);


  // Publication date
  // Posted date
  // Stamps
  // Tags

  const handleSetDescription = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDescription(event.target.value);
  }

  const handleSetLatitude = (event: React.ChangeEvent<HTMLInputElement>) => {
    const lat = Number.parseFloat(event.target.value);
    setLatitude(lat);
  }

  const handleSetLongitude = (event: React.ChangeEvent<HTMLInputElement>) => {
    const lng = Number.parseFloat(event.target.value);
    setLongitude(lng);
  }

  const handleSetCurrentView = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCurrentView(event.target.value);
  }

  const handleSetNotes = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setNotes(event.target.value);
  }

  const handleSetFlag = (event: React.ChangeEvent<HTMLInputElement>) => {
    const source = event.target.value;
    const checked = event.target.checked;
    setCardFlags({ ...cardFlags, [source]: checked });
  }

  const handleSetRecipientName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRecipientName(event.target.value);
  }

  const handleSetRecipientAddress = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setRecipientAddress(event.target.value);
  }

  const handleSetFrontImage = (event: React.ChangeEvent<HTMLInputElement>) => {
    const imageIndex = Number.parseInt(event.target.value);
    setFrontImage(imageIndex);
  }

  const handleSetFrontImageAltText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setFrontImageAltText(event.target.value);
  }

  const handleSetRearImage = (event: React.ChangeEvent<HTMLInputElement>) => {
    const imageIndex = Number.parseInt(event.target.value);
    setRearImage(imageIndex);
  }

  const handleSetRearImageAltText = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    setRearImageAltText(event.target.value);
  }

  const handleSetPublicationDescription = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPublicationDescription(event.target.value);
  }

  const handleSetPublisher = (event: React.ChangeEvent<HTMLInputElement>) => {
    const publisherId = Number.parseInt(event.target.value);
    setPublisher(publisherId);
  }

  const handleSetSeries = (event: React.ChangeEvent<HTMLInputElement>) => {
    const seriesId = Number.parseInt(event.target.value);
    setSeries(seriesId);
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Title>Create a new card</Title>

      <Section>
        <Title>About the subject</Title>
        <FormFields>
          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="description">Description</Label>
            <TextField
              id="description"
              name="description"
              placeholder="card title / description"
              value={description}
              onChange={handleSetDescription}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="front-image">Front Image</Label>
            {
              frontImage
                ? <img src={`/images/postcards/${frontImage}/front.jpg`} />
                : <FileUpload />
            }
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="front-image-alt">Front Alt Text</Label>
            <TextArea
              id="front-image-alt"
              name="front-image-alt"
              placeholder="alt text"
              rows={3}
              value={frontImageAltText}
              onChange={handleSetFrontImageAltText}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="rear-image">Rear Image</Label>
            {
              rearImage
                ? <img src={`/images/postcards/${rearImage}/rear.jpg`} />
                : <FileUpload />
            }
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="rear-image-alt">Rear Alt Text</Label>
            <TextArea
              id="rear-image-alt"
              name="rear-image-alt"
              placeholder="alt text"
              rows={3}
              value={rearImageAltText}
              onChange={handleSetRearImageAltText}
            />
          </div>

          <div className="sm:col-span-2 sm:col-start-1">
            <Label htmlFor="latitude">Latitude</Label>
            <TextField
              id="latitude"
              name="latitude"
              placeholder="lat"
              value={latitude}
              onChange={handleSetLatitude}
            />
          </div>

          <div className="sm:col-span-2">
            <Label htmlFor="longitude">Longitude</Label>
            <TextField
              id="longitude"
              name="longitude"
              placeholder="lat"
              value={longitude}
              onChange={handleSetLongitude}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="description">Current View</Label>
            <TextField
              id="current-view"
              name="current-view"
              placeholder="current view"
              value={currentView}
              onChange={handleSetCurrentView}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="notes">Notes</Label>
            <TextArea
              id="notes"
              name="notes"
              placeholder="notes"
              rows={3}
              value={notes}
              onChange={handleSetNotes}
            />
          </div>
        </FormFields>
      </Section>

      <Section>

        <Title>About the card</Title>

        <FieldSet>
          <Legend>Attibutes</Legend>

          <CheckBoxField>
            <CheckBox
              id="draft"
              value="draft"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.draft}
            />
            <div>
              <Label htmlFor="draft">Draft</Label>
              <Paragraph small>Is the card a draft (i.e. not publicly viewable)?</Paragraph>
            </div>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox
              id="rp"
              value="rp"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.rp}
            />
            <div>
              <Label htmlFor="rp">RP</Label>
              <Paragraph small>Is the card RP?</Paragraph>
            </div>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox
              id="used"
              value="used"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.used}
            />
            <div>
              <Label htmlFor="used">Used</Label>
              <Paragraph small>Is the card used?</Paragraph>
            </div>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox
              id="posted"
              value="posted"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.posted}
            />
            <div>
              <Label htmlFor="posted">Posted</Label>
              <Paragraph small>Was the card posted?</Paragraph>
            </div>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox
              id="franked"
              value="franked"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.franked}
            />
            <div>
              <Label htmlFor="franked">Franked</Label>
              <Paragraph small>Is the card franked?</Paragraph>
            </div>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox
              id="divided-back"
              value="divided-back"
              name="flags"
              onChange={handleSetFlag}
              defaultChecked={cardFlags.divided_back}
            />
            <div>
              <Label htmlFor="divided-back">Divided back</Label>
              <Paragraph small>Does the card have a divided back?</Paragraph>
            </div>
          </CheckBoxField>
        </FieldSet>

        <FormFields>
          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="publishers-description">Publisher's Description</Label>
            <TextField
              id="publishers-description"
              name="publishers-description"
              placeholder="publisher's description"
              value={publicationDescription}
              onChange={handleSetPublicationDescription}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="publisher">Publisher</Label>
            <TextField
              id="publisher"
              name="publisher"
              placeholder="publisher"
              value={publisher.name}
              onChange={handleSetPublisher}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="series">Series</Label>
            <TextField
              id="series"
              name="series"
              placeholder="series"
              value={series.name}
              onChange={handleSetSeries}
            />
          </div>
        </FormFields>
      </Section>

      <Section>
        <Title>About the recipient</Title>
        <FormFields>
          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="recipient-name">Name</Label>
            <TextField
              id="recipient-name"
              name="recipient-name"
              placeholder="recipient name"
              value={recipientName}
              onChange={handleSetRecipientName}
            />
          </div>

          <div className="sm:col-span-4 sm:col-start-1">
            <Label htmlFor="recipient-address">Address</Label>
            <TextArea
              id="recipient-address"
              name="recipient-address"
              placeholder="recipient address"
              rows={3}
              value={recipientAddress}
              onChange={handleSetRecipientAddress}
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

export default CardForm;