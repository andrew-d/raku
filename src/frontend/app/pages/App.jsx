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
      <div className='page-wrapper'>
        <Navbar fluid staticTop>
          <Navbar.Header>
            <Navbar.Brand>
              <IndexLink to='/'>Raku</IndexLink>
            </Navbar.Brand>
          </Navbar.Header>

          <Nav>
            <IndexLinkContainer to='/'>
              <NavItem href='/'>Home</NavItem>
            </IndexLinkContainer>
            <LinkContainer to='/about'>
              <NavItem href='/about'>About</NavItem>
            </LinkContainer>
          </Nav>
        </Navbar>

        <div className='container-fluid'>
          {this.props.children}
        </div>
      </div>
    );
  }
}
