import { TabMenu } from 'primereact/tabmenu';
import { MenuItem } from 'primereact/menuitem';
import { Link, UNSAFE_NavigationContext, useLocation, useNavigate, useNavigation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import NavBar from './NavBar';
import { Menu } from './menu/Menu';

export default function Header() {

  const navigate = useNavigate();
  const location = useLocation();
  const idx = location?.state?.idx || 0;

  const [activeIndex, setActiveIndex] = useState(idx);

  useEffect(() => {
    console.log("Effect setting index to " + idx);
    setActiveIndex(idx);
  }, [idx]);

  const items: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: 'pi pi-home',
      command: () => navigate({ pathname: '/' }, { state: { idx: 0 } })
    },
    {
      label: 'Transactions',
      icon: 'pi pi-chart-line',
      command: () => navigate({ pathname: '/details/1' }, { state: { idx: 1 } })
    },
    {
      label: 'Products',
      icon: 'pi pi-list',
      command: () => navigate({ pathname: '/notes' }, { state: { idx: 2 } })
    },
    {
      label: 'Messages',
      icon: 'pi pi-inbox',
      command: () => navigate({ pathname: '/links' }, { state: { idx: 3 } })
    }
  ];

  const links = [
    { title: "A menu item", url: "The url" },
    { title: "Another item", url: "Another url" }
  ];

  return (
    <div>
      {/* <TabMenu model={items} activeIndex={activeIndex}
        onTabChange={(e) => { console.log("Setting active index to " + e.index); setActiveIndex(e.index) }} />
      <div>Active index: {activeIndex}</div> */}
      <div>
        <NavBar />
        <Menu links={links} >
        </Menu>
      </div>
    </div>
  );
}
