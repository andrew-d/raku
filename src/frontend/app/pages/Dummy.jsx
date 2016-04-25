import React, { PropTypes } from 'react';


export class Dummy extends React.Component {
  static propTypes = {
    children: PropTypes.any,
  }

  render() {
    return this.props.children;
  }
}

export default Dummy;
