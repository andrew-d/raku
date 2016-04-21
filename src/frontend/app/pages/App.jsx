import React, { PropTypes } from 'react';
import { Link, IndexLink } from 'react-router';
import { Nav, Navbar, NavItem } from 'react-bootstrap';
import { LinkContainer, IndexLinkContainer } from 'react-router-bootstrap';


export default class App extends React.Component {
  static propTypes = {
    children: PropTypes.any,
  }

  render() {
    return (
      <div>
        <div className='page-wrapper'>
          <Navbar fluid staticTop>
            <Navbar.Header>
              <Navbar.Brand>
                <IndexLink to='/'>Raku</IndexLink>
              </Navbar.Brand>
            </Navbar.Header>

            <Nav>
              {this._renderNavbarItems([
                { href: '/',          text: 'Home', index: true },
                { href: '/documents', text: 'Documents' },
                { href: '/tags',      text: 'Tags' },
                { href: '/about',     text: 'About' },
              ])}
            </Nav>
          </Navbar>

          <div className='container-fluid'>
            {this.props.children}
          </div>
        </div>

        {this._renderDevTools()}
      </div>
    );
  }

  _renderNavbarItems(items) {
    let counter = 0;

    return items.map(item => {
      const ni = <NavItem href={item.href}>{item.text}</NavItem>;
      const key = 'navitem-' + (counter++);

      if (item.index) {
        return <IndexLinkContainer key={key} to={item.href}>{ni}</IndexLinkContainer>;
      }

      return <LinkContainer key={key} to={item.href}>{ni}</LinkContainer>;
    });
  }

  // Render Redux DevTools only in non-production builds.
  _renderDevTools() {
    if (__DEV__) {
      const DevTools = require('components/DevTools').default;
      return <DevTools />;
    }

    return null;
  }
}
