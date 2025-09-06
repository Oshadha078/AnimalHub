document.addEventListener('DOMContentLoaded', function () {
  const inputs = document.querySelectorAll('.form-control');
  inputs.forEach(input => {
    input.addEventListener('focus', function(){ this.parentElement.classList.add('focused'); });
    input.addEventListener('blur', function(){ this.parentElement.classList.remove('focused'); });
  });

  // Optional: support ?tab=signup etc.
  const params = new URLSearchParams(window.location.search);
  const tab = params.get('tab');
  if (tab) {
    const trigger = document.querySelector(`[data-bs-target="#${tab}"]`);
    if (trigger) new bootstrap.Tab(trigger).show();
  }
});
