import React from 'react';

import { styles } from './styles.scss';


export default class About extends React.Component {
  render() {
    return (
      <div className={styles}>
        <h2>About</h2>

        <p>
          This is the About page - TODO: add more info here.
        </p>
      </div>
    );
  }
}
