const Button = ({ children, variant = "primary", onClick, href }) => {
  const base =
    "inline-flex items-center px-6 py-3 rounded-md font-semibold transition duration-200";

  const variants = {
    primary: "bg-blue-600 text-white hover:bg-blue-700 cursor-pointer",
    secondary: "bg-gray-100 text-gray-800 hover:bg-gray-200 cursor-pointer",
  };

  const classes = `${base} ${variants[variant]}`;

  if (href)
    return (
      <a href={href} className={classes}>
        {children}
      </a>
    );
  return (
    <button onClick={onClick} className={classes}>
      {children}
    </button>
  );
};

export default Button;
