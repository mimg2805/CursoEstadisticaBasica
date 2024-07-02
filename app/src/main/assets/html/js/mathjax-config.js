window.MathJax = {
  startup: {
    ready: () => {
      MathJax.config.chtml.fontURL = "../fonts";
      MathJax.config.chtml.font.options.fontURL = "../fonts";
      MathJax.startup.defaultReady();
    }
  }
}