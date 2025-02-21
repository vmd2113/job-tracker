```jsx
const [isEnabled, setIsEnabled] = useState(true)
return (
    <>
        <Toggle
            isOn={isEnabled}
            onChange={() => setIsEnabled(!isEnabled)}
            label="Notifications"
        />
    </>

)
```

```html
<div class="toggle-container">
  <input type="checkbox" id="toggle" class="toggle-checkbox" />
  <label for="toggle" class="toggle-label"></label>
</div>

<style>
  .toggle-container {
    display: inline-block;
    position: relative;
  }

  .toggle-checkbox {
    display: none;
  }

  .toggle-label {
    display: block;
    width: 50px;
    height: 25px;
    background-color: #ccc;
    border-radius: 25px;
    position: relative;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .toggle-label::after {
    content: '';
    width: 20px;
    height: 20px;
    background-color: white;
    border-radius: 50%;
    position: absolute;
    top: 2.5px;
    left: 2.5px;
    transition: transform 0.3s;
  }

  .toggle-checkbox:checked + .toggle-label {
    background-color: #4caf50;
  }

  .toggle-checkbox:checked + .toggle-label::after {
    transform: translateX(25px);
  }
</style>

```