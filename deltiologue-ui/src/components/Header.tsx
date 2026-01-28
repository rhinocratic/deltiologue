import NavBar from "./NavBar";
import { Link, NavigateFunction } from "react-router-dom";
import { TabMenu } from 'primereact/tabmenu';
import { MenuItem } from 'primereact/menuitem';
import { useNavigate } from "react-router-dom";


export default function Header() {

  const navigate: NavigateFunction = useNavigate();

  const items: MenuItem[] = [
    { label: 'Home', icon: 'pi pi-home', command: () => navigate('/') },
    { label: 'Notes', icon: 'pi pi-home', command: () => navigate('/notes') },
    { label: 'Links', icon: 'pi pi-home', command: () => navigate('/links') }
  ];

  return (
    <div>
      <TabMenu model={items}>

      </TabMenu>
      {/* <div>
        <NavBar />
      </div> */}
    </div>
  );
}
