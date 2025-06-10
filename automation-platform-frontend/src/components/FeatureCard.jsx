const FeatureCard = ({ title, description, icon }) => {
  return (
    <div className="flex flex-col items-start p-6 border rounded-lg shadow-sm bg-white hover:shadow-md transition">
      <div className="text-blue-600 text-3xl mb-4">{icon}</div>
      <h3 className="text-lg font-semibold mb-2">{title}</h3>
      <p className="text-gray-600">{description}</p>
    </div>
  );
};

export default FeatureCard;
